import 'package:flutter/material.dart';
import 'TimeRow.dart';
import 'TransactionRow.dart';
import '../Utils/Transaction.dart';

class TransactionContainer extends StatelessWidget {
  TransactionContainer(
      {this.listSlideAnimation,
      this.listSlidePosition,
      this.listTileWidth,
      this.transactions});

  final Animation<double> listTileWidth;
  final Animation<Alignment> listSlideAnimation;
  final Animation<EdgeInsets> listSlidePosition;
  final List<Transaction> transactions;

  @override
  Widget build(BuildContext context) {
    List<Widget> children = [];

    transactions.sort((a, b) => b.time.compareTo(a.time));

    double margin = 0.5;
    for (int i = transactions.length - 1; i >= 0; i--) {
      Transaction transaction = transactions[i];

      if (i == transactions.length - 1 &&
          !TimeRow.isSameDay(transactions[i - 1].time, transaction.time)) {
        children.add(new TransactionRow(
          width: listTileWidth.value,
          margin: listSlidePosition.value * (++margin),
          transaction: transaction,
          applyBoxShadow: true,
        ));

        children.add(new TimeRow(
            width: listTileWidth.value,
            margin: listSlidePosition.value * (++margin),
            time: transactions[i].time));

        margin -= 0.52;
      } else if (i < transactions.length - 2 &&
          !TimeRow.isSameDay(transactions[i + 1].time, transaction.time)) {
        children.add(new TimeRow(
            width: listTileWidth.value,
            margin: listSlidePosition.value * (++margin),
            time: transactions[i + 1].time));

        margin += 0.48;

        children.add(new TransactionRow(
          width: listTileWidth.value,
          margin: listSlidePosition.value * margin,
          transaction: transaction,
          applyBoxShadow: true,
        ));
      } else {
        children.add(new TransactionRow(
          width: listTileWidth.value,
          margin: listSlidePosition.value * (++margin),
          transaction: transaction,
          applyBoxShadow: i == transactions.length - 2,
        ));
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
