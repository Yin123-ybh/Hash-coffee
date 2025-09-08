<template>
  <div class="login">
    <!-- 背景图片 -->
    <div class="login-background">
      <img src="@/assets/login/login-l.png" alt="Hash咖啡背景" />
    </div>

    <!-- 登录表单覆盖层 -->
    <div class="login-overlay">
      <div class="login-container">
        <!-- Logo区域 -->
        <div class="logo-section">
          <img
            src="@/assets/login/icon_logo.png"
            alt="Hash咖啡"
            class="logo"
          />
          <h1 class="brand-title">
            Hash咖啡
          </h1>
          <p class="brand-subtitle">
            用哈希的唯一性，定义咖啡的独特味
          </p>
        </div>

        <!-- 登录表单 -->
        <div class="form-section">
          <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
            <div class="form-title">
              <h2>欢迎回来</h2>
              <p>请登录您的账户</p>
            </div>

            <el-form-item prop="username">
              <el-input
                v-model="loginForm.username"
                type="text"
                auto-complete="off"
                placeholder="请输入账号"
                prefix-icon="el-icon-user"
                class="custom-input"
              />
            </el-form-item>

            <el-form-item prop="password">
              <el-input
                v-model="loginForm.password"
                type="password"
                placeholder="请输入密码"
                prefix-icon="el-icon-lock"
                class="custom-input"
                @keyup.enter.native="handleLogin"
              />
            </el-form-item>

            <el-form-item>
              <el-button
                :loading="loading"
                class="login-btn"
                type="primary"
                @click.native.prevent="handleLogin"
              >
                <span v-if="!loading">立即登录</span>
                <span v-else>登录中...</span>
              </el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from 'vue-property-decorator'
import { Route } from 'vue-router/types/router'
import { Form as ElForm, Input } from 'element-ui'
import { UserModule } from '@/store/modules/user'
import { isValidUsername } from '@/utils/validate'

@Component({
  name: 'Login'
})
export default class extends Vue {
  private validateUsername = (rule: any, value: string, callback: Function) => {
    if (!value) {
      callback(new Error('请输入用户名'))
    } else {
      callback()
    }
  }
  private validatePassword = (rule: any, value: string, callback: Function) => {
    if (value.length < 6) {
      callback(new Error('密码必须在6位以上'))
    } else {
      callback()
    }
  }
  private loginForm = {
    username: 'admin',
    password: '123456'
  } as {
    username: String
    password: String
  }

  loginRules = {
    username: [{ validator: this.validateUsername, trigger: 'blur' }],
    password: [{ validator: this.validatePassword, trigger: 'blur' }]
  }
  private loading = false
  private redirect?: string

  @Watch('$route', { immediate: true })
  private onRouteChange(route: Route) {}

  // 登录
  private handleLogin() {
    (this.$refs.loginForm as ElForm).validate(async (valid: boolean) => {
      if (valid) {
        this.loading = true
        await UserModule.Login(this.loginForm as any)
          .then((res: any) => {
            if (String(res.code) === '1') {
              this.$router.push('/')
            } else {
              // this.$message.error(res.msg)
              this.loading = false
            }
          })
          .catch(() => {
            // this.$message.error('用户名或密码错误！')
            this.loading = false
          })
      } else {
        return false
      }
    })
  }
}
</script>

<style lang="scss">
/* 版本: v2.0 - 全新美观登录界面 */
.login {
  position: relative;
  width: 100vw;
  height: 100vh;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
}

// 背景图片
.login-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    object-position: center;
  }
}

// 覆盖层
.login-overlay {
  position: relative;
  z-index: 2;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(0, 0, 0, 0.3);
  backdrop-filter: blur(2px);
}

// 登录容器
.login-container {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 20px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  padding: 40px;
  max-width: 450px;
  width: 90%;
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

// Logo区域
.logo-section {
  text-align: center;
  margin-bottom: 30px;

  .logo {
    width: 120px;
    height: 30px;
    margin-bottom: 15px;
    object-fit: contain;
  }

  .brand-title {
    font-size: 28px;
    font-weight: 600;
    color: #2c3e50;
    margin: 0 0 8px 0;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
  }

  .brand-subtitle {
    font-size: 14px;
    color: #7f8c8d;
    margin: 0;
    font-weight: 300;
  }
}

// 表单区域
.form-section {
  .form-title {
    text-align: center;
    margin-bottom: 30px;

    h2 {
      font-size: 24px;
      font-weight: 600;
      color: #2c3e50;
      margin: 0 0 8px 0;
    }

    p {
      font-size: 14px;
      color: #7f8c8d;
      margin: 0;
    }
  }
}

// 登录表单样式
.login-form {
  .el-form-item {
    margin-bottom: 25px;
  }

  .custom-input {
    .el-input__inner {
      height: 50px;
      border: 2px solid #e1e8ed;
      border-radius: 12px;
      font-size: 16px;
      padding: 0 20px 0 50px;
      background: #f8f9fa;
      transition: all 0.3s ease;

      &:focus {
        border-color: #667eea;
        background: #fff;
        box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
      }

      &::placeholder {
        color: #a0aec0;
        font-size: 14px;
      }
    }

    .el-input__prefix {
      left: 15px;
      color: #a0aec0;
      font-size: 18px;
    }

    .el-input--prefix .el-input__inner {
      padding-left: 50px;
    }
  }

  .el-form-item.is-error .el-input__inner {
    border-color: #e53e3e !important;
    background: #fff5f5 !important;
  }
}

// 登录按钮
.login-btn {
  width: 100%;
  height: 50px;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  border: none;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
  }

  &:active {
    transform: translateY(0);
  }

  &.is-loading {
    background: linear-gradient(135deg, #a0aec0 0%, #718096 100%);
  }
}

// 响应式设计
@media (max-width: 768px) {
  .login-container {
    padding: 30px 25px;
    max-width: 400px;
    margin: 20px;
  }

  .logo-section {
    margin-bottom: 25px;

    .logo {
      width: 100px;
      height: 25px;
    }

    .brand-title {
      font-size: 24px;
    }
  }

  .form-section .form-title h2 {
    font-size: 20px;
  }
}

@media (max-width: 480px) {
  .login-container {
    padding: 25px 20px;
    margin: 15px;
    border-radius: 15px;
  }

  .logo-section {
    margin-bottom: 20px;

    .logo {
      width: 90px;
      height: 22px;
    }

    .brand-title {
      font-size: 22px;
    }

    .brand-subtitle {
      font-size: 13px;
    }
  }

  .form-section .form-title {
    margin-bottom: 25px;

    h2 {
      font-size: 18px;
    }

    p {
      font-size: 13px;
    }
  }

  .login-form {
    .custom-input .el-input__inner {
      height: 45px;
      font-size: 15px;
    }

    .login-btn {
      height: 45px;
      font-size: 15px;
    }
  }
}
</style>
