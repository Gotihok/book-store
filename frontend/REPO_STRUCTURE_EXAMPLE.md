/src/app
├── core                     # App-wide singletons
│   ├── auth
│   │   ├── auth.service.ts
│   │   ├── auth.guard.ts
│   │   └── auth.interceptor.ts
│   ├── error
│   │   └── global-error-handler.ts
│   └── core.module.ts
│
├── shared                   # Reusable, stateless
│   ├── components
│   │   ├── navbar/
│   │   └── spinner/
│   ├── directives
│   ├── pipes
│   └── shared.module.ts
│
├── features
│   ├── user
│   │   ├── pages
│   │   │   ├── user-profile.page.ts
│   │   │   └── user-settings.page.ts
│   │   ├── components
│   │   ├── services
│   │   │   └── user.service.ts
│   │   ├── state
│   │   │   ├── user.store.ts
│   │   │   └── user.effects.ts
│   │   ├── user.routes.ts
│   │   └── user.module.ts
│   ├── products
│   │   ├── pages
│   │   ├── components
│   │   ├── services
│   │   ├── state
│   │   ├── products.routes.ts
│   │   └── products.module.ts
│   └── admin
│       └── ...
│
├── app.component.ts
├── app.routes.ts
└── app.config.ts
