# ==== paths ====
NGX_ROOT := $(PWD)/ops/nginx
NGX_CONF := conf/nginx.conf
NGX_BIN  ?= nginx          # 如非 brew，可改绝对路径

# ==== ports ====
NGX_PORT ?= 18081          # Nginx 监听端口
APP_PORT ?= 18080          # Spring Boot 端口

# ==== helpers ====
define _say
	@echo "👉 $1"
endef

.PHONY: up reload stop quit down test status logs kill init help

## 初始化（创建必要目录，防止 -p 下 logs 不存在）
init:
	@mkdir -p "$(NGX_ROOT)/logs"
	@touch "$(NGX_ROOT)/logs/access.log" "$(NGX_ROOT)/logs/error.log" || true

## 启动 Nginx（项目内前缀，不碰全局）
up: init
	$(call _say,Check config ...)
	$(NGX_BIN) -p "$(NGX_ROOT)" -c "$(NGX_CONF)" -t
	$(call _say,Starting Nginx with -p $(NGX_ROOT) -c $(NGX_CONF) ...)
	$(NGX_BIN) -p "$(NGX_ROOT)" -c "$(NGX_CONF)"
	$(call _say,Nginx is up. Open http://localhost:$(NGX_PORT))

## 热重载配置（重载前先校验）
reload:
	$(call _say,Validate config ...)
	$(NGX_BIN) -p "$(NGX_ROOT)" -c "$(NGX_CONF)" -t
	$(call _say,Reloading Nginx ...)
	$(NGX_BIN) -p "$(NGX_ROOT)" -c "$(NGX_CONF)" -s reload

## 停止（立即停止）
stop:
	$(call _say,Stopping Nginx (stop) ...)
	-$(NGX_BIN) -p "$(NGX_ROOT)" -c "$(NGX_CONF)" -s stop || true

## 优雅退出（处理完连接后退出）
quit:
	$(call _say,Graceful quit Nginx ...)
	-$(NGX_BIN) -p "$(NGX_ROOT)" -c "$(NGX_CONF)" -s quit || true

## 优雅失败后强制停止（CI/本地救急）
down: quit
	@sleep 1
	@pgrep -af "nginx: master process" | grep "$(NGX_ROOT)" >/dev/null 2>&1 && \
	 (echo "Force killing project nginx..." && pgrep -af "nginx.*$(NGX_ROOT)" | awk '{print $$1}' | xargs -r kill -9) || \
	 echo "No project nginx to kill."

## 校验配置语法
test:
	$(NGX_BIN) -p "$(NGX_ROOT)" -c "$(NGX_CONF)" -t

## 查看监听与进程（macOS）
status:
	@echo "🔎 Listening on $(NGX_PORT) ?"
	@lsof -i :$(NGX_PORT) | grep LISTEN || echo "No process is listening on $(NGX_PORT)"
	@echo "🔎 Spring Boot expected on $(APP_PORT) ?"
	@lsof -i :$(APP_PORT) | grep LISTEN || echo "No process is listening on $(APP_PORT)"
	@echo "🔎 Project nginx processes (prefix=$(NGX_ROOT)):"
	@pgrep -af "nginx: master process" | grep "$(NGX_ROOT)" || echo "No project nginx running."
	@echo "🔎 Any global nginx?"
	@pgrep -af "nginx: master process" | grep -v "$(NGX_ROOT)" || echo "No global nginx."

## 跟随查看项目内 Nginx 访问/错误日志
logs:
	@echo "📜 tail logs"; \
	for f in access.log error.log; do \
	  if [ -f "$(NGX_ROOT)/logs/$$f" ]; then echo "--- $(NGX_ROOT)/logs/$$f ---"; tail -n 50 -f "$(NGX_ROOT)/logs/$$f"; else echo "No $(NGX_ROOT)/logs/$$f"; fi; \
	done

## 仅杀本项目 nginx（避免误伤系统全局）
kill:
	@echo "❌ Killing project nginx (prefix=$(NGX_ROOT))"
	@pgrep -af "nginx.*$(NGX_ROOT)" | awk '{print $$1}' | xargs -r kill -9 || true

## 帮助
help:
	@echo "Targets:"
	@echo "  make up       - 启动项目内 Nginx（-p/-c）"
	@echo "  make reload   - 重载配置（先校验）"
	@echo "  make stop     - 立即停止"
	@echo "  make quit     - 优雅退出"
	@echo "  make down     - 优雅失败后强制停"
	@echo "  make test     - 语法检查"
	@echo "  make status   - 端口/进程检查（含全局冲突）"
	@echo "  make logs     - 跟随日志"
	@echo "  make kill     - 仅杀本项目 nginx（前缀过滤）"
