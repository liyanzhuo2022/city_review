# 🏙️ City Life Review Platform

*A high-performance backend for discovering, reviewing, and engaging with local businesses.*

---

## 🚀 Overview

**City Life Review Platform** is a backend system designed for a modern local-business discovery and review app.

It supports **multi-category merchants** (restaurants, cafes, shops, services), allowing users to log in, explore nearby businesses, receive vouchers, and interact through check-ins and reviews.

The project emphasizes **high concurrency**, **caching**, **distributed systems concepts**, and **cloud deployment**, with Redis as a core component of the performance architecture.

> ⚠️ The project is actively under development. The README reflects completed work as well as planned extensions.
> 

---

## ✨ Core Features

### ✅ **User Authentication (Completed)**

- Implemented SMS login using Spring Security + JWT
- Stateless sessions
- Token generation + verification + expiration handling
- Designed extensible authentication pipeline for future OAuth login

---

### 🏪 **Merchant Listing & Basic Business Features (In Progress)**

Current progress:

- Data model for merchants
- Standard CRUD APIs
- Basic listing & filtering

Planned:

- Geo-based search (Redis GEOHASH)
- Ranking & personalized suggestion feed

---

### 💬 **Customer Reviews (In Progress)**

- Review posting + listing
- Store rating calculation
- Review filtering & pagination
- Planned: spam detection + “helpful” clicks

---

### 🎟️ **Voucher & Flash Sale System (Completed)**

- Redis counters for stock tracking
- Lua scripts for atomic operations
- Async order queue for high-concurrency seckill
- Distributed lock to prevent overselling
- Covers three Redis MQ models: List / PubSub / Stream

---

### 📦 **Caching Layer (Completed)**

Redis-based caching for merchant queries and hot data.

Implemented:

- Cache penetration protection
- Cache breakdown prevention (logical expiration)
- Cache avalanche mitigation strategies
- Reusable caching utilities

---

### 🔒 **Distributed Locks (Completed)**

- Custom Redis-based distributed lock implementation
- Lock renewal (“watchdog”) mechanism
- Used in flash-sale workflows

---

### 🧭 **Nearby Merchants (Planned)**

Using Redis GEO:

- Save merchant locations
- Query by radius
- Return sorted results by distance

---

### 🧍 **User Check-In (Planned)**

Using Redis BitMap:

- Daily check-ins
- Monthly streak calculation
- Efficient presence queries

---

### 👥 **Follow System (Planned)**

Using Redis SET:

- Follow / Unfollow
- Mutual follow detection (set intersection)
- Follow feed (optional)

---

### 📊 **Analytics (Planned)**

Using Redis HyperLogLog (HLL):

- UV statistics
- Lightweight traffic analytics

---

## 🧰 Tech Stack

### **Backend**

- Java 17
- Spring Boot
- Spring MVC
- Spring Security + JWT
- JPA / Hibernate

### **Database**

- MySQL
- Flyway for schema migrations

### **Caching & Performance**

- Redis
- Lua scripts
- Redis GEO / Set / Bitmap / HLL
- Redis Streams + List + Pub/Sub

### **DevOps & Deployment**

- Docker / Docker Compose
- Nginx
- AWS EC2 + RDS
- IAM / CloudWatch
- GitHub Actions CI/CD

### **Testing (Planned)**

- Unit tests
- Integration tests

---

## 📦 Code Structure

```
.
├── Dockerfile                # Container image for the backend service
├── Makefile                  # Helper commands (build, run, deploy)
├── docker-compose.yml        # Local orchestration: app + MySQL + Redis + Nginx
│
├── ops/                      # Ops & infrastructure configuration
│   ├── flyway/               # Database migration scripts (V1__init.sql, etc.)
│   └── nginx/                # Nginx reverse proxy & gateway configs
│
├── pom.xml                   # Maven dependencies & project configuration
│
├── src/
│   ├── main/                 # Application source code
│   │   ├── java/...          # Controllers, services, entities, repositories
│   │   └── resources/        # application.yml, log configs, Flyway setup
│   └── test/                 # Unit & integration tests (JUnit, Testcontainers)
│
└── target/                   # Build artifacts (auto-generated)
    ├── classes/              # Compiled class files
    ├── hm-dianping-0.0.1-SNAPSHOT.jar  # Packaged application
    └── test-classes/         # Compiled test classes
```

---

## 🚀 Getting Started

### 1. Clone the project

```bash
git clone https://github.com/liyanzhuo2022/city_review.git
cd city_review
```

### **2. Run the backend**

You can run the backend in two ways:

### **Local (recommended for development)**

```bash
mvn spring-boot:run
```

### **Docker (dev mode)**

```bash
mvn package -DskipTests
docker compose --profile dev up -d
```

Use this URL:

```
http://localhost:18081
```

---

## 🧠 What I Learned

- Building a **production-style backend** using Spring Boot, JPA, Redis
- Designing **high-concurrency workflows** with Lua + Redis + async queues
- Implementing **JWT authentication** and understanding Spring Security internals
- Applying **Redis data structures** in real scenarios (SET, GEO, Stream, Bitmap)
- Managing schema evolution with **Flyway**
- Containerizing applications via Docker
- Deploying to **AWS EC2/RDS**, setting up logs & monitoring
- Building CI/CD pipelines with GitHub Actions

This project significantly strengthened my backend engineering and distributed-systems thinking.

---

## 🔮 Future Roadmap

- [ ]  Geo-based merchant search
- [ ]  UV analytics with HyperLogLog
- [ ]  User check-in (Bitmap)
- [ ]  Full follow system + friend-of-friend graph
- [ ]  Timeline feed + ranking
- [ ]  Observability (Actuator + logs + metrics)
- [ ]  Test coverage (unit + integration + Testcontainers)
- [ ]  Kubernetes deployment on AWS/EKS

---

## 📜 License

MIT
