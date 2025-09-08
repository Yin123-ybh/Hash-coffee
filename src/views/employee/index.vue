<template>
  <div class="dashboard-container">
    <div class="container">
      <!-- 查询栏 -->
      <div class="tableBar">
        <label style="margin-right: 5px">员工姓名：</label>
        <el-input
          v-model="input"
          placeholder="请输入员工姓名"
          style="width: 15%"
          clearable
          @clear="init"
          @keyup.enter.native="initFun"
        />
        <el-button class="normal-btn continue" @click="init(true)">
          查询
        </el-button>
        <el-button
          type="primary"
          style="float: right"
          @click="addEmployeeHandle('add')"
        >
          + 添加员工
        </el-button>
      </div>

      <!-- 员工表格 -->
      <el-table
        v-if="tableData.length"
        :data="tableData"
        stripe
        class="tableBox"
      >
        <el-table-column prop="name" label="员工姓名" />
        <el-table-column prop="username" label="账号" />
        <el-table-column prop="phone" label="手机号" />

        <!-- 账号状态 -->
        <el-table-column v-slot="scope" label="账号状态">
          <div
            class="tableColumn-status"
            :class="{ 'stop-use': String(scope.row.status) === '0' }"
          >
            {{ String(scope.row.status) === '0' ? '禁用' : '启用' }}
          </div>
        </el-table-column>

        <el-table-column prop="updateTime" label="最后操作时间" />

        <!-- 操作列 -->
        <el-table-column v-slot="scope" label="操作" width="160" align="center">
          <el-button
            type="text"
            size="small"
            class="blueBug"
            :class="{ 'disabled-text': scope.row.username === 'admin' }"
            :disabled="scope.row.username === 'admin'"
            @click="addEmployeeHandle(scope.row.id, scope.row.username)"
          >
            修改
          </el-button>
          <el-button
            :disabled="scope.row.username === 'admin'"
            type="text"
            size="small"
            class="non"
            :class="{
              'disabled-text': scope.row.username === 'admin',
              blueBug: scope.row.status == '0',
              delBut: scope.row.status != '0'
            }"
            @click="statusHandle(scope.row)"
          >
            {{ scope.row.status == '1' ? '禁用' : '启用' }}
          </el-button>
        </el-table-column>
      </el-table>
      <!-- 空数据提示 -->
      <Empty v-else :is-search="isSearch" />

      <!-- 分页 -->
      <el-pagination
        class="pageList"
        :page-sizes="[10, 20, 30, 40]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="counts"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    <!-- Add the missing closing tag for .container above -->
  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import HeadLable from '@/components/HeadLable/index.vue'
import { getEmployeeList, enableOrDisableEmployee } from '@/api/employee'
import { UserModule } from '@/store/modules/user'
import InputAutoComplete from '@/components/InputAutoComplete/index.vue'
import Empty from '@/components/Empty/index.vue'

@Component({
  name: 'Employee',
  components: {
    HeadLable,
    InputAutoComplete,
    Empty
  }
})
export default class Employee extends Vue {
  private input: string = ''
  private counts: number = 0
  private page: number = 1
  private pageSize: number = 10
  private tableData: any[] = []
  private id: string = ''
  private status: string = ''
  private isSearch: boolean = false

  created() {
    this.init()
  }

  initProp(val: string) {
    this.input = val
    this.initFun()
  }

  initFun() {
    this.page = 1
    this.init()
  }

  get userName() {
    return UserModule.username
  }

  private async init(isSearch?: boolean) {
    this.isSearch = isSearch || false
    const params = {
      page: this.page,
      pageSize: this.pageSize,
      name: this.input || undefined
    }
    try {
      const res: any = await getEmployeeList(params)
      if (String(res.data.code) === '1') {
       this.tableData = (res.data && res.data.data && res.data.data.records) || []
       this.counts = (res.data && res.data.data && res.data.data.total) || 0
      }
    } catch (err) {
      this.$message.error('请求出错了：' + err.message)
    }
  }

  // 添加/修改员工
  private addEmployeeHandle(st: string, username?: string) {
    if (st === 'add') {
      this.$router.push({ path: '/employee/add' })
    } else {
      if (username === 'admin') return
      this.$router.push({ path: '/employee/add', query: { id: st } })
    }
  }

  // 状态修改
  private statusHandle(row: any) {
    if (row.username === 'admin') return
    this.id = row.id
    this.status = row.status
    this.$confirm('确认调整该账号的状态?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(async () => {
      try {
        const res = await enableOrDisableEmployee({
          id: this.id,
          status: !this.status ? 1 : 0
        })
        if (String(res.status) === '200') {
          this.$message.success('账号状态更改成功！')
          this.init()
        }
      } catch (err) {
        this.$message.error('请求出错了：' + err.message)
      }
    })
  }

  private handleSizeChange(val: number) {
    this.pageSize = val
    this.init()
  }

  private handleCurrentChange(val: number) {
    this.page = val
    this.init()
  }
}
</script>

<style lang="scss" scoped>
.disabled-text {
  color: #bac0cd !important;
}
.stop-use {
  color: red;
}
.blueBug {
  color: blue;
}
.delBut {
  color: orange;
}
.tableColumn-status {
  font-weight: bold;
}
</style>
