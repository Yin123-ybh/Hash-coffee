<template>
  <div class="app-container">
    <div class="filter-container">
      <el-date-picker
        v-model="dateRange"
        type="daterange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        value-format="yyyy-MM-dd"
        class="filter-item"
        style="width: 300px;"
        @change="handleDateChange"
      />
      <el-button
        v-waves
        class="filter-item"
        type="primary"
        icon="el-icon-search"
        @click="handleFilter"
      >
        查询
      </el-button>
    </div>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="statistics-cards">
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="card-content">
            <div class="card-icon">
              <i class="iconfont icon-quan"></i>
            </div>
            <div class="card-info">
              <div class="card-title">优惠券总数</div>
              <div class="card-value">{{ statisticsData.totalCoupons }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="card-content">
            <div class="card-icon used">
              <i class="iconfont icon-quan"></i>
            </div>
            <div class="card-info">
              <div class="card-title">已使用</div>
              <div class="card-value">{{ statisticsData.usedCoupons }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="card-content">
            <div class="card-icon expired">
              <i class="iconfont icon-quan"></i>
            </div>
            <div class="card-info">
              <div class="card-title">已过期</div>
              <div class="card-value">{{ statisticsData.expiredCoupons }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="card-content">
            <div class="card-icon discount">
              <i class="iconfont icon-tongji"></i>
            </div>
            <div class="card-info">
              <div class="card-title">优惠金额</div>
              <div class="card-value">¥{{ statisticsData.totalDiscount }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="chart-container">
      <el-col :span="12">
        <el-card>
          <div slot="header" class="card-header">
            <span>优惠券使用趋势</span>
          </div>
          <div ref="usageChart" class="chart"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header" class="card-header">
            <span>优惠券类型分布</span>
          </div>
          <div ref="typeChart" class="chart"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 秒杀活动统计 -->
    <el-card class="seckill-statistics">
      <div slot="header" class="card-header">
        <span>秒杀活动统计</span>
      </div>
      <el-table
        :data="seckillData"
        border
        style="width: 100%"
      >
        <el-table-column label="活动名称" prop="name" min-width="150" />
        <el-table-column label="商品名称" prop="productName" min-width="120" />
        <el-table-column label="原价" align="center" width="100">
          <template slot-scope="{row}">
            ¥{{ row.originalPrice }}
          </template>
        </el-table-column>
        <el-table-column label="秒杀价" align="center" width="100">
          <template slot-scope="{row}">
            ¥{{ row.seckillPrice }}
          </template>
        </el-table-column>
        <el-table-column label="库存" align="center" width="100">
          <template slot-scope="{row}">
            {{ row.soldCount }}/{{ row.stock }}
          </template>
        </el-table-column>
        <el-table-column label="销售率" align="center" width="100">
          <template slot-scope="{row}">
            {{ ((row.soldCount / row.stock) * 100).toFixed(1) }}%
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
            <el-tag :type="getSeckillStatusTag(row)">
              {{ getSeckillStatusText(row) }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { couponStatisticsApi } from '@/api/coupon'
import waves from '@/directive/waves'
import * as echarts from 'echarts'

export default {
  name: 'CouponStatistics',
  directives: { waves },
  data() {
    return {
      dateRange: [],
      statisticsData: {
        totalCoupons: 0,
        usedCoupons: 0,
        expiredCoupons: 0,
        totalDiscount: 0
      },
      seckillData: [],
      usageChart: null,
      typeChart: null
    }
  },
  created() {
    this.initDateRange()
    this.getStatistics()
    this.getSeckillData()
  },
  mounted() {
    this.$nextTick(() => {
      this.initCharts()
    })
  },
  methods: {
    initDateRange() {
      const end = new Date()
      const start = new Date()
      start.setMonth(start.getMonth() - 1)
      this.dateRange = [
        start.toISOString().split('T')[0],
        end.toISOString().split('T')[0]
      ]
    },
    async getStatistics() {
      try {
        const params = {
          startTime: this.dateRange[0],
          endTime: this.dateRange[1]
        }
        const response = await couponStatisticsApi.getStatistics(params)
        if (response.data.code === 1) {
          this.statisticsData = response.data.data
          this.updateCharts()
        }
      } catch (error) {
        console.error(error)
      }
    },
    async getSeckillData() {
      try {
        const params = {
          startTime: this.dateRange[0],
          endTime: this.dateRange[1]
        }
        const response = await couponStatisticsApi.getSeckillStatistics(params)
        if (response.data.code === 1) {
          this.seckillData = response.data.data
        }
      } catch (error) {
        console.error(error)
      }
    },
    handleDateChange() {
      this.getStatistics()
      this.getSeckillData()
    },
    handleFilter() {
      this.getStatistics()
      this.getSeckillData()
    },
    initCharts() {
      this.initUsageChart()
      this.initTypeChart()
    },
    initUsageChart() {
      const chartDom = this.$refs.usageChart
      this.usageChart = echarts.init(chartDom)
      this.updateUsageChart()
    },
    initTypeChart() {
      const chartDom = this.$refs.typeChart
      this.typeChart = echarts.init(chartDom)
      this.updateTypeChart()
    },
    updateCharts() {
      this.updateUsageChart()
      this.updateTypeChart()
    },
    updateUsageChart() {
      if (this.usageChart) {
        const option = {
          title: {
            text: '优惠券使用趋势',
            left: 'center'
          },
          tooltip: {
            trigger: 'axis'
          },
          xAxis: {
            type: 'category',
            data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
          },
          yAxis: {
            type: 'value'
          },
          series: [{
            name: '使用数量',
            type: 'line',
            data: [120, 200, 150, 80, 70, 110, 130],
            smooth: true,
            itemStyle: {
              color: '#409EFF'
            }
          }]
        }
        this.usageChart.setOption(option)
      }
    },
    updateTypeChart() {
      if (this.typeChart) {
        const option = {
          title: {
            text: '优惠券类型分布',
            left: 'center'
          },
          tooltip: {
            trigger: 'item'
          },
          series: [{
            name: '优惠券类型',
            type: 'pie',
            radius: '50%',
            data: [
              { value: 335, name: '满减券' },
              { value: 310, name: '折扣券' },
              { value: 234, name: '代金券' }
            ],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }]
        }
        this.typeChart.setOption(option)
      }
    },
    getSeckillStatusText(row) {
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
    getSeckillStatusTag(row) {
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

<style scoped>
.statistics-cards {
  margin-bottom: 20px;
}

.statistics-card {
  margin-bottom: 20px;
}

.card-content {
  display: flex;
  align-items: center;
  padding: 20px;
}

.card-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
  background: #409EFF;
  color: white;
  font-size: 24px;
}

.card-icon.used {
  background: #67C23A;
}

.card-icon.expired {
  background: #F56C6C;
}

.card-icon.discount {
  background: #E6A23C;
}

.card-info {
  flex: 1;
}

.card-title {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.card-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.chart-container {
  margin-bottom: 20px;
}

.chart {
  width: 100%;
  height: 300px;
}

.card-header {
  font-weight: bold;
  font-size: 16px;
}

.seckill-statistics {
  margin-top: 20px;
}
</style>
