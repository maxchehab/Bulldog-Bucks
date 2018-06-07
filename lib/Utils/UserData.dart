import 'Transaction.dart';

enum MealPlan { Platinum, Gold, Silver, Blue, White }

class UserData {
  UserData(
      {this.balance,
      this.frozen,
      this.transactions,
      this.mealPlan,
      this.swipesRemaining});

  final double balance;
  final bool frozen;
  final List<Transaction> transactions;
  final MealPlan mealPlan;
  final int swipesRemaining;
}
