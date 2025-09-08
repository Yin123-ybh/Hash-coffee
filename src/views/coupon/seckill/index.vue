<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.name"
        placeholder="活动名称"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
      <el-select
        v-model="listQuery.status"
        placeholder="状态"
        clearable
        style="width: 120px"
        class="filter-item"
      >
        <el-option label="启用" :value="1" />
        <el-option label="禁用" :value="0" />
      </el-select>
      <el-date-picker
        v-model="listQuery.startTime"
        type="date"
        placeholder="开始时间"
        value-format="yyyy-MM-dd"
        style="width: 150px"
        class="filter-item"
      />
      <el-date-picker
        v-model="listQuery.endTime"
        type="date"
        placeholder="结束时间"
        value-format="yyyy-MM-dd"
        style="width: 150px"
        class="filter-item"
      />
      <el-button
        v-waves
        class="filter-item"
        type="primary"
        icon="el-icon-search"
        @click="handleFilter"
      >
        搜索
      </el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="primary"
        @click="handleCreate"
      >
        <i class="iconfont icon-seckill-activity" style="margin-right: 5px;"></i>
        新增秒杀活动
      </el-button>
    </div>

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
    >
      <el-table-column label="ID" prop="id" sortable="custom" align="center" width="80">
        <template slot-scope="{row}">
          <span>{{ row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column label="活动名称" prop="name" min-width="150">
        <template slot-scope="{row}">
          <span>{{ row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="商品名称" prop="dishName" min-width="120">
        <template slot-scope="{row}">
          <span>{{ row.dishName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="价格" align="center" width="120">
        <template slot-scope="{row}">
          <div style="text-decoration: line-through; color: #999;">
            <i class="iconfont icon-quan" style="margin-right: 3px;"></i>
            ¥{{ row.originalPrice }}
          </div>
          <div style="color: #f56c6c; font-weight: bold;">
            <i class="iconfont icon-huodong" style="margin-right: 3px;"></i>
            ¥{{ row.seckillPrice }}
          </div>
        </template>
      </el-table-column>
      <el-table-column label="库存" align="center" width="100">
        <template slot-scope="{row}">
          <span>{{ row.soldCount }}/{{ row.stock }}</span>
        </template>
      </el-table-column>
      <el-table-column label="限购数量" align="center" width="100">
        <template slot-scope="{row}">
          <span>{{ row.perUserLimit }}件</span>
        </template>
      </el-table-column>
      <el-table-column label="活动时间" align="center" width="200">
        <template slot-scope="{row}">
          <div>{{ row.startTime }}</div>
          <div>{{ row.endTime }}</div>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" width="100">
        <template slot-scope="{row}">
          <el-tag :type="getStatusTag(row)">
            {{ getStatusText(row) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button type="info" size="mini" @click="handleView(row)">
            查看
          </el-button>
          <el-button type="danger" size="mini" @click="handleDelete(row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.size"
      @pagination="getList"
    />

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogStatus === 'create' ? '新增秒杀活动' : '编辑秒杀活动'"
      :visible.sync="dialogFormVisible"
      width="600px"
    >
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="temp"
        label-position="left"
        label-width="120px"
        style="width: 100%;"
      >
        <el-form-item label="活动名称" prop="name">
          <el-input v-model="temp.name" placeholder="请输入活动名称" />
        </el-form-item>
        <el-form-item label="活动描述" prop="description">
          <el-input
            v-model="temp.description"
            type="textarea"
            :rows="3"
            placeholder="请输入活动描述"
          />
        </el-form-item>
        <el-form-item label="商品" prop="dishId">
          <el-select
            v-model="temp.dishId"
            placeholder="请选择商品"
            style="width: 100%;"
            @change="handleProductChange"
          >
            <el-option
              v-for="item in productList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="原价" prop="originalPrice">
          <el-input-number
            v-model="temp.originalPrice"
            :precision="2"
            :min="0"
            :max="999999"
            placeholder="请输入原价"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="秒杀价格" prop="seckillPrice">
          <el-input-number
            v-model="temp.seckillPrice"
            :precision="2"
            :min="0"
            :max="999999"
            placeholder="请输入秒杀价格"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="秒杀库存" prop="stock">
          <el-input-number
            v-model="temp.stock"
            :min="1"
            :max="999999"
            placeholder="请输入秒杀库存"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="每人限购" prop="perUserLimit">
          <el-input-number
            v-model="temp.perUserLimit"
            :min="1"
            :max="999"
            placeholder="请输入每人限购数量"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker
            v-model="temp.startTime"
            type="datetime"
            placeholder="选择开始时间"
            value-format="yyyy-MM-ddTHH:mm:ss"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker
            v-model="temp.endTime"
            type="datetime"
            placeholder="选择结束时间"
            value-format="yyyy-MM-ddTHH:mm:ss"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="temp.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus === 'create' ? createData() : updateData()">
          确定
        </el-button>
      </div>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog
      title="秒杀活动详情"
      :visible.sync="viewDialogVisible"
      width="600px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="活动名称">
          {{ viewData.name }}
        </el-descriptions-item>
        <el-descriptions-item label="活动状态">
          <el-tag :type="getStatusTag(viewData)">
            {{ getStatusText(viewData) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="商品名称">
          {{ viewData.dishName }}
        </el-descriptions-item>
        <el-descriptions-item label="原价">
          ¥{{ viewData.originalPrice }}
        </el-descriptions-item>
        <el-descriptions-item label="秒杀价格">
          ¥{{ viewData.seckillPrice }}
        </el-descriptions-item>
        <el-descriptions-item label="库存">
          {{ viewData.soldCount }}/{{ viewData.stock }}
        </el-descriptions-item>
        <el-descriptions-item label="每人限购">
          {{ viewData.perUserLimit }}件
        </el-descriptions-item>
        <el-descriptions-item label="开始时间">
          {{ viewData.startTime }}
        </el-descriptions-item>
        <el-descriptions-item label="结束时间">
          {{ viewData.endTime }}
        </el-descriptions-item>
        <el-descriptions-item label="活动描述" :span="2">
          {{ viewData.description }}
        </el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="viewDialogVisible = false">
          关闭
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { seckillApi } from '@/api/coupon'
import { queryDishList } from '@/api/dish'
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'SeckillActivity',
  components: { Pagination },
  directives: { waves },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        size: 10,
        name: '',
        status: '',
        startTime: '',
        endTime: ''
      },
      temp: {
        id: '',
        name: '',
        description: '',
        dishId: '',
        dishName: '',
        seckillPrice: 0,
        originalPrice: 0,
        stock: 0,
        soldCount: 0,
        perUserLimit: 1,
        startTime: '',
        endTime: '',
        status: 1
      },
      dialogFormVisible: false,
      dialogStatus: '',
      rules: {
        name: [{ required: true, message: '请输入活动名称', trigger: 'blur' }],
        dishId: [{ required: true, message: '请选择商品', trigger: 'change' }],
        seckillPrice: [{ required: true, message: '请输入秒杀价格', trigger: 'blur' }],
        originalPrice: [{ required: true, message: '请输入原价', trigger: 'blur' }],
        stock: [{ required: true, message: '请输入秒杀库存', trigger: 'blur' }],
        perUserLimit: [{ required: true, message: '请输入每人限购数量', trigger: 'blur' }],
        startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
        endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }]
      },
      viewDialogVisible: false,
      viewData: {},
      productList: []
    }
  },
  created() {
    this.getList()
    this.getProductList()
  },
  methods: {
    async getList() {
      this.listLoading = true
      try {
        const response = await seckillApi.getPage(this.listQuery)
        if (response.data.code === 1) {
          this.list = response.data.data.records
          this.total = response.data.data.total
        }
      } catch (error) {
        console.error(error)
      } finally {
        this.listLoading = false
      }
    },
    async getProductList() {
      try {
        const response = await queryDishList({ status: 1 })
        if (response.data.code === 1) {
          this.productList = response.data.data
        }
      } catch (error) {
        console.error(error)
      }
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    resetTemp() {
      this.temp = {
        id: '',
        name: '',
        description: '',
        dishId: '',
        dishName: '',
        seckillPrice: 0,
        originalPrice: 0,
        stock: 0,
        soldCount: 0,
        perUserLimit: 1,
        startTime: '',
        endTime: '',
        status: 1
      }
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs.dataForm.clearValidate()
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs.dataForm.clearValidate()
      })
    },
    handleView(row) {
      this.viewData = Object.assign({}, row)
      this.viewDialogVisible = true
    },
    handleProductChange(dishId) {
      const product = this.productList.find(item => item.id === dishId)
      if (product) {
        this.temp.dishName = product.name
        this.temp.originalPrice = product.price
      }
    },
    async createData() {
      this.$refs.dataForm.validate(async (valid) => {
        if (valid) {
          try {
            const response = await seckillApi.save(this.temp)
            if (response.data.code === 1) {
              this.dialogFormVisible = false
              this.$message.success('创建成功')
              this.getList()
            } else {
              this.$message.error(response.data.msg)
            }
          } catch (error) {
            console.error(error)
            this.$message.error('创建失败，请重试')
          }
        }
      })
    },
    async updateData() {
      this.$refs.dataForm.validate(async (valid) => {
        if (valid) {
          try {
            const response = await seckillApi.update(this.temp.id, this.temp)
            if (response.data.code === 1) {
              this.dialogFormVisible = false
              this.$message.success('更新成功')
              this.getList()
            } else {
              this.$message.error(response.data.msg)
            }
          } catch (error) {
            console.error(error)
            this.$message.error('更新失败，请重试')
          }
        }
      })
    },
    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除这个秒杀活动吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const response = await seckillApi.delete(row.id)
        if (response.data.code === 1) {
          this.$message.success('删除成功')
          this.getList()
        } else {
          this.$message.error(response.data.msg)
        }
      } catch (error) {
        console.error(error)
      }
    },
    getStatusText(row) {
      const now = new Date()
      const startTime = new Date(row.startTime)
      const endTime = new Date(row.endTime)
      
      if (row.status === 0) {
        return '已禁用'
      } else if (now < startTime) {
        return '未开始'
      } else if (now > endTime) {
        return '已结束'
      } else {
        return '进行中'
      }
    },
    getStatusTag(row) {
      const now = new Date()
      const startTime = new Date(row.startTime)
      const endTime = new Date(row.endTime)
      
      if (row.status === 0) {
        return 'danger'
      } else if (now < startTime) {
        return 'info'
      } else if (now > endTime) {
        return 'warning'
      } else {
        return 'success'
      }
    }
  }
}
</script>
