import 'package:flutter/material.dart';
import 'TimeRow.dart';
import 'TransactionRow.dart';
import 'Transaction.dart';

class ListViewContent extends StatelessWidget {
  final Animation<double> listTileWidth;
  final Animation<Alignment> listSlideAnimation;
  final Animation<EdgeInsets> listSlidePosition;
  ListViewContent({
    this.listSlideAnimation,
    this.listSlidePosition,
    this.listTileWidth,
  });

  @override
  Widget build(BuildContext context) {
    List<Widget> children = [];

    List<Transaction> transactions = [
      new Transaction(
          TransactionType.Sale, 12.0, "Aloha Island Grill", new DateTime.now()),
      new Transaction(
          TransactionType.Sale,
          12.0,
          "Bruchi's CheaseStakes & Subs",
          new DateTime.now().subtract(new Duration(days: 1))),
      new Transaction(TransactionType.Sale, 12.0, "Carls Jr",
          new DateTime.now().subtract(new Duration(days: 2))),
      new Transaction(TransactionType.Sale, 12.0, "Caruso's",
          new DateTime.now().subtract(new Duration(days: 3))),
      new Transaction(TransactionType.Sale, 12.0, "Clarks Fork",
          new DateTime.now().subtract(new Duration(days: 4))),
      new Transaction(TransactionType.Sale, 12.0, "Domino's Pizza",
          new DateTime.now().subtract(new Duration(days: 6))),
    ];

    transactions.sort((a, b) => b.time.compareTo(a.time));

    double margin = 0.5;
    for (int i = transactions.length - 1; i >= 0; i--) {
      Transaction transaction = transactions[i];

      if (transaction.name == "Domino's Pizza") {
        debugPrint("DOMIN-HOES: " + (i < transactions.length - 2).toString());
      }

      if (i == transactions.length - 1 &&
          !TimeRow.isSameDay(transactions[i - 1].time, transaction.time)) {
        children.add(new TransactionRow(
            width: listTileWidth.value,
            margin: listSlidePosition.value * (++margin),
            transaction: transaction));

        children.add(new TimeRow(
            width: listTileWidth.value,
            margin: listSlidePosition.value * (++margin),
            time: transactions[i].time));

        margin -= 0.75;
      } else if (i < transactions.length - 2 &&
          !TimeRow.isSameDay(transactions[i + 1].time, transaction.time)) {
        children.add(new TimeRow(
            width: listTileWidth.value,
            margin: listSlidePosition.value * (++margin),
            time: transactions[i + 1].time));

        margin += 0.25;

        children.add(new TransactionRow(
            width: listTileWidth.value,
            margin: listSlidePosition.value * margin,
            transaction: transaction));
      } else {
        children.add(new TransactionRow(
            width: listTileWidth.value,
            margin: listSlidePosition.value * (++margin),
            transaction: transaction));
      }

      if (i == 0) {
        children.add(new TimeRow(
            width: listTileWidth.value,
            margin: listSlidePosition.value * (++margin),
            time: transaction.time));
      }
    }

    return (new Stack(alignment: listSlideAnimation.value, children: children));
  }
}
