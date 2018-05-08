import 'package:flutter/material.dart';
import 'CalenderCell.dart';

class Calender extends StatelessWidget {
  final EdgeInsets margin;
  final DateTime startTime;
  Calender({this.margin, this.startTime});

  @override
  Widget build(BuildContext context) {
    List<CalenderCell> cells = [];
    int distantDays = DateTime.now().difference(startTime).inDays;
    for (int i = distantDays; i > -1; i--) {
      cells.add(new CalenderCell(
          time: new DateTime.now().subtract(new Duration(days: i))));
    }

    return (new Container(
      margin: margin,
      alignment: Alignment.center,
      padding: new EdgeInsets.only(top: 20.0),
      decoration: new BoxDecoration(
        color: Colors.white,
        border: new Border(
          bottom: new BorderSide(
              width: 1.0, color: const Color.fromRGBO(204, 204, 204, 1.0)),
        ),
      ),
      child: new SingleChildScrollView(
        scrollDirection: Axis.horizontal,
        child: new Row(children: cells),
      ),
    ));
  }
}
