# Grocery Hub Android App (Declarative Gradle)

This Android app demonstrates a grocery shopping experience:
- Browse products
- View product details
- Add items to the cart
- Manage cart quantities
- Place orders for delivery or pickup

Tech:
- Kotlin, AndroidX, Material Design
- MVVM (ViewModel + LiveData)
- RecyclerView for lists
- Local mock data
- Cart persistence via SharedPreferences
- Declarative Gradle (Gradle 9 prototype)

Build:
- ./gradlew build

Install/run:
- ./gradlew :app:installDebug
- Launch "Grocery Hub"

Navigation:
- Splash screen -> Main with bottom nav (Products | Cart)
- Product tap -> Product details (with Add to Cart)
- Cart -> Checkout -> Enter address (for delivery) -> Place order
