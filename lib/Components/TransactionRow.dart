import 'package:flutter/material.dart';

import '../Utils/Transaction.dart';

class TransactionRow extends StatelessWidget {
  final EdgeInsets margin;
  final double width;
  final Transaction transaction;
  final bool applyBoxShadow;

  TransactionRow(
      {this.width, this.margin, this.transaction, this.applyBoxShadow = false});

  @override
  Widget build(BuildContext context) {
    return (new Container(
      alignment: Alignment.center,
      margin: margin,
      width: width,
      decoration: new BoxDecoration(
          color: Colors.white,
          border: new Border(
            top: new BorderSide(width: 0.5, color: Colors.black12),
          ),
          boxShadow: [
            new BoxShadow(
              color: Colors.black26,
              blurRadius: applyBoxShadow ? 2.0 : 0.0,
            ),
          ]),
      child: new Row(
        children: <Widget>[
          new Container(
              margin: new EdgeInsets.only(
                  left: 20.0, top: 10.0, bottom: 10.0, right: 20.0),
              width: 60.0,
              height: 60.0,
              decoration: new BoxDecoration(
                  shape: BoxShape.circle, image: transaction.location.image)),
          new Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: <Widget>[
              new Text(
                "\$" + transaction.amount.toStringAsFixed(2),
                style: new TextStyle(
                    fontSize: 18.0,
                    fontWeight: FontWeight.w400,
                    color: (transaction.type == TransactionType.Sale
                        ? Colors.redAccent
                        : Colors.greenAccent)),
              ),
              new Padding(
                padding: new EdgeInsets.only(top: 5.0),
                child: new Text(
                  transaction.name,
                  style: new TextStyle(
                      color: Colors.grey,
                      fontSize: 14.0,
                      fontWeight: FontWeight.w300),
                ),
              )
            ],
          )
        ],
      ),
    ));
  }
}
