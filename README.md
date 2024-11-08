
# Expense Tracker

An **Expense Tracker** application designed to help users manage their personal finances by tracking income and expenses. This project is developed using Java Swing for the graphical user interface and SQLite for data storage.

## Features

- Add, view, and delete expense entries
- Track income and expenses with category selection
- Generate summaries for monthly, weekly, or custom date ranges
- Interactive and user-friendly interface with Java Swing
- Persistent data storage using SQLite

## Project Structure

- **ExpenseTracker Folder**
  - Contains all Java files for application logic and UI.
  - `lib` folder containing SQLite JDBC (`sqlite-jdbc-3.47.0.0.jar`) for database connectivity.

## Prerequisites

- **Java Development Kit (JDK)**: Java 8 or higher
- **SQLite**: Pre-installed or packaged with the JDBC library
- **SQLite JDBC Library**: The application requires `sqlite-jdbc-3.47.0.0.jar`, located in the `lib` folder.

## Setup and Installation

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   ```

2. **Set up the SQLite database**:
   - Ensure the SQLite JDBC library is available by placing `sqlite-jdbc-3.47.0.0.jar` in the `lib` folder.
   - The database file will be created automatically when running the application for the first time.

3. **Compile and run the application**:
   ```bash
   javac -cp .:lib/sqlite-jdbc-3.47.0.0.jar Main.java
   java -cp .:lib/sqlite-jdbc-3.47.0.0.jar Main
   ```

4. **Launch the application**: You can start adding expenses and tracking your finances.

## Usage

1. **Add Expense**: Enter expense details, such as amount, category, and description.
2. **View Expenses**: Display a list of all expenses with filtering options.
3. **Generate Reports**: View summaries based on selected date ranges or categories.

## Technologies Used

- **Java Swing**: User Interface
- **SQLite**: Database management
- **SQLite JDBC**: JDBC driver for SQLite database

## Future Enhancements

- Data export to CSV format
- Enhanced data visualization with charts and graphs
- Budget and savings tracking

## Contributing

Contributions are welcome! Please follow these steps:

1. **Fork the repository**
2. **Create a new branch**:
   ```bash
   git checkout -b feature-branch
   ```
3. **Commit your changes**:
   ```bash
   git commit -am 'Add new feature'
   ```
4. **Push to the branch**:
   ```bash
   git push origin feature-branch
   ```
5. **Open a Pull Request**

## License

This project is open-source and available under the [Pradip1010100 License](LICENSE).
