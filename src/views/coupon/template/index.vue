<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.name"
        placeholder="优惠券名称"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
      <el-select
        v-model="listQuery.type"
        placeholder="优惠券类型"
        clearable
        style="width: 150px"
        class="filter-item"
      >
        <el-option
          v-for="item in couponTypes"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
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
        <i class="iconfont icon-coupon-template" style="margin-right: 5px;"></i>
        新增优惠券模板
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
      <el-table-column label="优惠券名称" prop="name" width="120">
        <template slot-scope="{row}">
          <span>{{ row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="类型" align="center" width="120">
        <template slot-scope="{row}">
          <el-tag :type="getCouponTypeTag(row.type)">
            <i :class="getCouponTypeIcon(row.type)" style="margin-right: 3px;"></i>
            {{ getCouponTypeText(row.type) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="优惠内容" align="center" width="120">
        <template slot-scope="{row}">
          <span v-if="row.type === 1">
            满{{ row.minAmount }}减{{ row.discountValue }}
          </span>
          <span v-else-if="row.type === 2">
            {{ row.discountValue }}折
          </span>
          <span v-else>
            {{ row.discountValue }}元
          </span>
        </template>
      </el-table-column>
      <el-table-column label="发放数量" align="center" width="150">
        <template slot-scope="{row}">
          <span>{{ row.usedCount }}/{{ row.totalCount }}</span>
        </template>
      </el-table-column>
      <el-table-column label="有效期" align="center" width="200">
        <template slot-scope="{row}">
          <div>{{ row.startTime }}</div>
          <div>{{ row.endTime }}</div>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" width="80">
        <template slot-scope="{row}">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'">
            {{ row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button type="success" size="mini" @click="handleSendCoupon(row)">
            发放
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
      :title="dialogStatus === 'create' ? '新增优惠券模板' : '编辑优惠券模板'"
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
        <el-form-item label="优惠券名称" prop="name">
          <el-input v-model="temp.name" placeholder="请输入优惠券名称" />
        </el-form-item>
        <el-form-item label="优惠券类型" prop="type">
          <el-radio-group v-model="temp.type">
            <el-radio :label="1">满减券</el-radio>
            <el-radio :label="2">折扣券</el-radio>
            <el-radio :label="3">代金券</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="优惠券描述" prop="description">
          <el-input
            v-model="temp.description"
            type="textarea"
            :rows="3"
            placeholder="请输入优惠券描述"
          />
        </el-form-item>
        <el-form-item label="优惠券图片" prop="image">
          <el-input v-model="temp.image" placeholder="请输入图片URL" />
        </el-form-item>
        <el-form-item label="优惠值" prop="discountValue">
          <el-input-number
            v-model="temp.discountValue"
            :precision="2"
            :min="0"
            :max="999999"
            placeholder="请输入优惠值"
          />
        </el-form-item>
        <el-form-item label="使用门槛" prop="minAmount">
          <el-input-number
            v-model="temp.minAmount"
            :precision="2"
            :min="0"
            :max="999999"
            placeholder="请输入使用门槛金额"
          />
        </el-form-item>
        <el-form-item label="发放总数量" prop="totalCount">
          <el-input-number
            v-model="temp.totalCount"
            :min="1"
            :max="999999"
            placeholder="请输入发放总数量"
          />
        </el-form-item>
        <el-form-item label="每人限领数量" prop="perUserLimit">
          <el-input-number
            v-model="temp.perUserLimit"
            :min="1"
            :max="999"
            placeholder="请输入每人限领数量"
          />
        </el-form-item>
        <el-form-item label="有效天数" prop="validDays">
          <el-input-number
            v-model="temp.validDays"
            :min="1"
            :max="365"
            placeholder="请输入有效天数"
          />
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker
            v-model="temp.startTime"
            type="datetime"
            placeholder="选择开始时间"
            format="yyyy-MM-dd HH:mm:ss"
            value-format="yyyy-MM-ddTHH:mm:ss"
            style="width: 100%;"
            :clearable="true"
          />
        </el-form-item>

        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker
            v-model="temp.endTime"
            type="datetime"
            placeholder="选择结束时间"
            format="yyyy-MM-dd HH:mm:ss"
            value-format="yyyy-MM-ddTHH:mm:ss"
            style="width: 100%;"
            :clearable="true"
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

    <!-- 发放优惠券对话框 -->
    <el-dialog
      title="发放优惠券"
      :visible.sync="sendDialogVisible"
      width="500px"
    >
      <el-form
        ref="sendForm"
        :rules="sendRules"
        :model="sendTemp"
        label-position="left"
        label-width="120px"
        style="width: 100%;"
      >
        <el-form-item label="优惠券模板">
          <el-input v-model="sendTemp.templateName" disabled />
        </el-form-item>
        <el-form-item label="发放数量" prop="count">
          <el-input-number
            v-model="sendTemp.count"
            :min="1"
            :max="999"
            placeholder="请输入发放数量"
          />
        </el-form-item>
        <el-form-item label="发放方式" prop="sendType">
          <el-radio-group v-model="sendTemp.sendType">
            <el-radio :label="1">指定用户</el-radio>
            <el-radio :label="2">所有用户</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="sendTemp.sendType === 1" label="用户ID" prop="userIdsText" :rules="sendRules.userIdsText">
          <el-input
            v-model="sendTemp.userIdsText"
            type="textarea"
            :rows="3"
            placeholder="请输入用户ID，多个用逗号分隔"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="sendDialogVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="sendCoupon">
          确定发放
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { couponTemplateApi, couponApi } from '@/api/coupon'
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'CouponTemplate',
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
        type: '',
        status: ''
      },
      temp: {
        id: '',
        name: '',
        type: 1,
        description: '',
        image: '',
        discountValue: 0,
        minAmount: 0,
        totalCount: 100,
        perUserLimit: 1,
        validDays: 30,
        startTime: null,
        endTime: null,
        status: 1
      },
      dialogFormVisible: false,
      dialogStatus: '',
      rules: {
        name: [{ required: true, message: '请输入优惠券名称', trigger: 'blur' }],
        type: [{ required: true, message: '请选择优惠券类型', trigger: 'change' }],
        discountValue: [{ required: true, message: '请输入优惠值', trigger: 'blur' }],
        totalCount: [{ required: true, message: '请输入发放总数量', trigger: 'blur' }],
        perUserLimit: [{ required: true, message: '请输入每人限领数量', trigger: 'blur' }],
        validDays: [{ required: true, message: '请输入有效天数', trigger: 'blur' }],
        startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
        endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }]
      },
      sendDialogVisible: false,
      sendTemp: {
        templateId: '',
        templateName: '',
        count: 1,
        sendType: 1,
        userIdsText: ''
      },
      sendRules: {
  count: [{ required: true, message: '请输入发放数量', trigger: 'blur' }],
  sendType: [{ required: true, message: '请选择发放方式', trigger: 'change' }],
  userIds: [
    { 
      validator: (rule, value, callback) => {
        if (this.sendTemp.sendType === 1 && (!value || value.trim() === '')) {
          callback(new Error('请输入用户ID'))
        } else {
          callback()
        }
      }, 
      trigger: 'blur' 
    }
  ],
  userIdsText: [
    { 
      validator: (rule, value, callback) => {
        if (this.sendTemp.sendType === 1) { // 指定用户模式
          if (!value || value.trim() === '') {
            callback(new Error('请输入用户ID'));
          } else {
            // 验证用户ID格式（数字和逗号）
            const userIds = value.split(',').map(id => id.trim());
            const invalidIds = userIds.filter(id => !/^\d+$/.test(id));
            if (invalidIds.length > 0) {
              callback(new Error('用户ID格式不正确，请输入数字'));
            } else {
              callback();
            }
          }
        } else {
          callback(); // 所有用户模式不需要验证
        }
      }, 
      trigger: 'blur' 
    }
  ]
},
      couponTypes: [
        { value: 1, label: '满减券' },
        { value: 2, label: '折扣券' },
        { value: 3, label: '代金券' }
      ]
    }
  },
  created() {
    this.getList()
  },
  methods: {
    async getList() {
      this.listLoading = true
      try {
        const response = await couponTemplateApi.getPage(this.listQuery)
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
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    resetTemp() {
      this.temp = {
        id: '',
        name: '',
        type: 1,
        description: '',
        image: '',
        discountValue: 0,
        minAmount: 0,
        totalCount: 100,
        perUserLimit: 1,
        validDays: 30,
        startTime: null,
        endTime: null,
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
  // 确保 status 字段正确初始化
  if (this.temp.status === undefined || this.temp.status === null) {
  this.temp.status = 1
}
  this.dialogStatus = 'update'
  this.dialogFormVisible = true
  this.$nextTick(() => {
    this.$refs.dataForm.clearValidate()
  })
},
    async createData() {
      this.$refs.dataForm.validate(async (valid) => {
        if (valid) {
          try {
            const data = {
              name: this.temp.name,
              type: this.temp.type,
              discountType: 1,
              discountValue: this.temp.discountValue,
              minAmount: this.temp.minAmount,
              maxDiscount: this.temp.maxDiscount || 0,
              totalCount: this.temp.totalCount,
              perUserLimit: this.temp.perUserLimit,
              validDays: this.temp.validDays,
              startTime: this.temp.startTime,
              endTime: this.temp.endTime,
              status: this.temp.status || 1
            }
            const response = await couponTemplateApi.save(data)
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
            const data = {
  id: this.temp.id,
  name: this.temp.name,
  type: this.temp.type,
  discountType: this.temp.discountType || 1,
  discountValue: this.temp.discountValue,
  minAmount: this.temp.minAmount,
  maxDiscount: this.temp.maxDiscount || 0,
  totalCount: this.temp.totalCount,
  perUserLimit: this.temp.perUserLimit,
  validDays: this.temp.validDays,
  startTime: this.temp.startTime || null,
  endTime: this.temp.endTime || null,
  status: this.temp.status,
  description: this.temp.description || '',
  image: this.temp.image || ''
}

console.log('提交的数据:', data) // 添加调试日志
            const response = await couponTemplateApi.update(data)
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
        await this.$confirm('确定要删除这个优惠券模板吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const response = await couponTemplateApi.delete(row.id)
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
    handleSendCoupon(row) {
      this.sendTemp = {
        templateId: row.id,
        templateName: row.name,
        count: 1,
        sendType: 1,
        userIdsText: ''
      }
      this.sendDialogVisible = true
      this.$nextTick(() => {
        this.$refs.sendForm.clearValidate()
      })
    },
    async sendCoupon() {
      this.$refs.sendForm.validate(async (valid) => {
        if (valid) {
          try {
            const data = {
  templateId: this.sendTemp.templateId,
  count: this.sendTemp.count,
  userIds: this.sendTemp.sendType === 1 
    ? this.sendTemp.userIdsText.split(',').map(id => parseInt(id.trim())).filter(id => !isNaN(id))
    : []
}
            const response = await couponApi.batchIssue(data)
            if (response.data.code === 1) {
              this.sendDialogVisible = false
              this.$message.success('发放成功')
              this.getList()
            } else {
              this.$message.error(response.data.msg)
            }
          } catch (error) {
  console.error('发放优惠券失败：', error)
  if (error.response && error.response.data && error.response.data.msg) {
    this.$message.error(error.response.data.msg)
  } else {
    this.$message.error('发放失败，请重试')
  }
}
        }
      })
    },
    getCouponTypeText(type) {
      const typeMap = {
        1: '满减券',
        2: '折扣券',
        3: '代金券'
      }
      return typeMap[type] || '未知'
    },
    getCouponTypeTag(type) {
      const tagMap = {
        1: 'success',
        2: 'warning',
        3: 'info'
      }
      return tagMap[type] || 'info'
    },
    getCouponTypeIcon(type) {
      const iconMap = {
        1: 'iconfont icon-quan',
        2: 'iconfont icon-quan',
        3: 'iconfont icon-quan'
      }
      return iconMap[type] || 'iconfont icon-coupon'
    }
  }
}
</script>
