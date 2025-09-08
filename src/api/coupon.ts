import request from '@/utils/request'

// 优惠券模板管理
export const couponTemplateApi = {
  // 分页查询优惠券模板
  getPage: (params: any) =>
    request({
      url: '/api/coupon/template/page',
      method: 'get',
      params
    }),
  
  // 根据ID查询优惠券模板
  getById: (id: number) =>
    request({
      url: `/api/coupon/template/${id}`,
      method: 'get'
    }),
  
  // 新增优惠券模板
  save: (data: any) =>
    request({
      url: '/api/coupon/template',
      method: 'post',
      data
    }),
  
  // 修改优惠券模板
  update: (data: any) =>
    request({
      url: '/api/coupon/template',
      method: 'put',
      data
    }),
  
  // 删除优惠券模板
  delete: (id: number) =>
    request({
      url: `/api/coupon/template/${id}`,
      method: 'delete'
    }),
  
  // 启用/禁用优惠券模板
  updateStatus: (id: number, status: number) =>
    request({
      url: `/api/coupon/template/status/${id}/${status}`,
      method: 'put'
    })
}

// 优惠券管理
export const couponApi = {
  // 分页查询优惠券
  getPage: (params: any) =>
    request({
      url: '/admin/coupon/page',
      method: 'get',
      params
    }),
  
  // 根据ID查询优惠券
  getById: (id: number) =>
    request({
      url: `/admin/coupon/${id}`,
      method: 'get'
    }),
  
  // 发放优惠券
  issue: (data: any) =>
    request({
      url: '/admin/coupon/issue',
      method: 'post',
      data
    }),
  
  // 批量发放优惠券
  batchIssue: (data: any) =>
    request({
      url: '/api/coupon/batch-issue',
      method: 'post',
      data
    }),
  
  // 作废优惠券
  invalidate: (id: number) =>
    request({
      url: `/admin/coupon/invalidate/${id}`,
      method: 'put'
    })
}

// 秒杀活动管理
export const seckillApi = {
  // 分页查询秒杀活动
  getPage: (params: any) =>
    request({
      url: '/api/seckill/page',
      method: 'get',
      params
    }),
  
  // 根据ID查询秒杀活动
  getById: (id: number) =>
    request({
      url: `/api/seckill/${id}`,
      method: 'get'
    }),
  
  // 新增秒杀活动
  save: (data: any) =>
    request({
      url: '/api/seckill',
      method: 'post',
      data
    }),
  
  // 修改秒杀活动
  update: (data: any) =>
    request({
      url: '/api/seckill',
      method: 'put',
      data
    }),
  
  // 删除秒杀活动
  delete: (id: number) =>
    request({
      url: `/api/seckill/${id}`,
      method: 'delete'
    }),
  
  // 启用/禁用秒杀活动
  updateStatus: (id: number, status: number) =>
    request({
      url: `/api/seckill/status/${id}/${status}`,
      method: 'put'
    }),
  
  // 获取菜品列表（用于秒杀活动选择）
  getDishList: () =>
    request({
      url: '/api/dish/list',
      method: 'get'
    })
}

// 优惠券统计
export const couponStatisticsApi = {
  // 获取优惠券统计数据
  getStatistics: (params: any) =>
    request({
      url: '/api/coupon/template/statistics',
      method: 'get',
      params
    }),
  
  // 获取优惠券使用趋势
  getUsageTrend: (params: any) =>
    request({
      url: '/api/coupon/template/usage-trend',
      method: 'get',
      params
    }),
  
  // 获取秒杀活动统计
  getSeckillStatistics: (params: any) =>
    request({
      url: '/api/seckill/statistics',
      method: 'get',
      params
    })
}

// 用户端优惠券接口
export const userCouponApi = {
  // 获取用户优惠券列表
  getUserCoupons: (params: any) =>
    request({
      url: '/user/coupon/list',
      method: 'get',
      params
    }),
  
  // 领取优惠券
  receive: (templateId: number) =>
    request({
      url: `/user/coupon/receive/${templateId}`,
      method: 'post'
    }),
  
  // 使用优惠券
  use: (couponId: number, orderId: number) =>
    request({
      url: `/user/coupon/use/${couponId}`,
      method: 'post',
      data: { orderId }
    }),
  
  // 获取可用优惠券
  getAvailable: (orderAmount: number) =>
    request({
      url: '/user/coupon/available',
      method: 'get',
      params: { orderAmount }
    })
}

// 用户端秒杀接口
export const userSeckillApi = {
  // 获取秒杀活动列表
  getSeckillList: (params: any) =>
    request({
      url: '/user/seckill/list',
      method: 'get',
      params
    }),
  
  // 获取秒杀活动详情
  getSeckillDetail: (id: number) =>
    request({
      url: `/user/seckill/${id}`,
      method: 'get'
    }),
  
  // 参与秒杀
  participate: (data: any) =>
    request({
      url: '/user/seckill/participate',
      method: 'post',
      data
    })
}
