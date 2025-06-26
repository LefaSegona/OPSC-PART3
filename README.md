# OPSC-PART3

Pocket Ninja Budgeting Application

Pocket Ninja is a comprehensive personal finance management application designed to help users track expenses, set budget goals, and manage their finances more effectively.  

Features

The application offers a range of features to support financial management. **User Management** allows users to register and log in with secure authentication. The **Dashboard** provides a financial overview, displaying income, expenses, and budget progress. With **Budget Tracking**, users can set monthly budget goals and monitor spending by category. **Expense Tracking** enables users to record and categorize transactions with detailed information. **Account Management** allows users to manage multiple financial accounts in one place, while **Category Management** lets them customize spending categories with icons and colors. Additionally, **Financial Insights** help users visualize spending patterns and track financial progress.  

Tech Stack

The application is built using modern technologies. The Frontend is developed with React.js and TypeScript, utilizing React Query for server state management and Wouter for client-side routing. Forms are handled with React Hook Form and validated using Zod. The UI is built with Shadcn UI components and Tailwind CSS, with icons from Lucide React.  

Database Structure

The database consists of several tables:  

- Users Table: Stores user information, including username, hashed password, email, and personal details.  
- Categories Table: Tracks spending categories with customizable icons, colors, and budget limits.  
- Accounts Table: Manages financial accounts, including names, types, balances, and optional icons.  
- Transactions Table: Records all transactions with details such as amount, description, date, and receipt images.  
- Budgets Table: Defines monthly and yearly budget goals with minimum and maximum spending limits.  

Setting Up the Database

The database is configured using Drizzle ORM To set it up:  

1. Establish a connection via the `DATABASE_URL` environment variable.  
2. Define the schema in `shared/schema.ts`.  
3. Push the schema to the database using `npm run db:push`.  
4. Database operations are implemented in `server/storage.ts` via the `DatabaseStorage` class.  

For Kotlin (Android) implementations, developers can use Room Database:  

1. Add Room dependencies in `build.gradle`.  
2. Create Entity Classes for each table.  
3. Define Data Access Objects (DAOs) for CRUD operations.  
4. Set up a Database Class and Repositories for clean architecture.  

Getting Started

To run the application:  

1. Clone the repository.  
2. Install dependencies with `npm install`.  
3. Configure the `.env` file with the `DATABASE_URL`.  
4. Initialize the database with `npm run db:push`.  
5. Start the development server using `npm run dev`.  

API Endpoints

The backend provides RESTful endpoints for:  

- Authentication: User registration, login, logout, and session management.  
- Categories: CRUD operations for spending categories.  
- Accounts: Managing financial accounts.  
- Transactions: Recording and modifying transactions.  
- Budgets: Setting and updating budget goals.  
- Stats: Retrieving spending, income, and category-based analytics.  

youtube URL: https://www.youtube.com/shorts/yjE5w9i77_Y
