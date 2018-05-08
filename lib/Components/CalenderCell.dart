import 'package:flutter/material.dart';

class CalenderCell extends StatelessWidget {
  final List<String> _weeks = [
    "MON",
    "TUE",
    "WED",
    "THU",
    "FRI",
    "SAT",
    "SUN",
  ];
  final DateTime time;

  String _week;
  String _day;
  bool _today;

  CalenderCell({this.time}) {
    this._week = _weeks[time.weekday - 1];
    this._day = time.day.toString();

    Duration difference = new DateTime.now().difference(time);
    this._today = difference.inDays == 0;
  }

  @override
  Widget build(BuildContext context) {
    return (new Container(
        margin: new EdgeInsets.symmetric(vertical: 0.0, horizontal: 10.0),
        child: new Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          children: <Widget>[
            new Text(
              _week,
              style: new TextStyle(
                  color: const Color.fromRGBO(204, 204, 204, 1.0),
                  fontSize: 12.0,
                  fontWeight: FontWeight.w400),
            ),
            new Padding(
              padding: new EdgeInsets.only(top: 10.0, bottom: 5.0),
              child: new Container(
                  width: 35.0,
                  height: 35.0,
                  alignment: Alignment.center,
                  decoration: new BoxDecoration(
                      shape: BoxShape.circle,
                      color: _today
                          ? const Color.fromRGBO(204, 204, 204, 0.3)
                          : Colors.transparent),
                  child: new Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: <Widget>[
                      new Text(
                        _day,
                        style: new TextStyle(
                            fontSize: 12.0, fontWeight: FontWeight.w400),
                      ),
                      _today
                          ? new Container(
                              padding: new EdgeInsets.only(top: 3.0),
                              width: 3.0,
                              height: 3.0,
                              decoration: new BoxDecoration(
                                  shape: BoxShape.circle,
                                  color:
                                      const Color.fromRGBO(247, 64, 106, 1.0)),
                            )
                          : new Container()
                    ],
                  )),
            )
          ],
        )));
  }
}
