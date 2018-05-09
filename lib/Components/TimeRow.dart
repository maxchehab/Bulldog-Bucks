import 'package:flutter/material.dart';

class TimeRow extends StatelessWidget {
  final EdgeInsets margin;
  final double width;
  final DateTime time;

  TimeRow({this.margin, this.width, this.time});

  @override
  Widget build(BuildContext context) {
    return (new Container(
      alignment: Alignment.center,
      margin: margin,
      width: width,
      decoration: new BoxDecoration(
        color: Colors.white,
        border: new Border(
          top: new BorderSide(
              width: 1.0, color: const Color.fromRGBO(204, 204, 204, 0.3)),
          bottom: new BorderSide(
              width: 1.0, color: const Color.fromRGBO(204, 204, 204, 0.3)),
        ),
      ),
      child: new Row(
        children: <Widget>[
          new Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: <Widget>[
              new Text(
                "Today",
                style: new TextStyle(
                    fontSize: 18.0,
                    fontWeight: FontWeight.w400,
                    color: Colors.red),
              )
            ],
          )
        ],
      ),
    ));
  }
}
