import 'Location.dart';

enum TransactionType { Sale, Deposit }

class Transaction {
  final TransactionType type;
  final String name;
  final double amount;
  final Location location;
  final DateTime time;
  final String accountType;
  final String status;
  final String deniedMessage;

  Transaction(
      {this.type,
      this.amount,
      this.time,
      String location,
      this.accountType,
      this.status,
      this.deniedMessage})
      : this.location = Location.findLocationFromKeyWords(location),
        this.name = Location.findLocationFromKeyWords(location).name;
}
