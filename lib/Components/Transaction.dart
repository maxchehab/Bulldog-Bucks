import 'Location.dart';
import '../Screens/Home/styles.dart';

enum TransactionType { Sale, Deposit }

class Transaction {
  TransactionType type;
  double amount;
  Location location;
  String name;
  DateTime time;

  Transaction(
      TransactionType type, double amount, String location, DateTime time) {
    this.type = type;
    this.amount = amount;
    this.time = time;
    this.name = location;
    this.location = Location.findLocationFromKeyWords(locations, location);
  }
}
