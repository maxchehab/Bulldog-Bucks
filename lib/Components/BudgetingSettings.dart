import 'package:flutter/material.dart';

class BudgetingSettings extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return (new Container(
        decoration: new BoxDecoration(
            border: new Border(
                bottom: new BorderSide(
          width: 0.5,
          color: Colors.white24,
        ))),
        padding: new EdgeInsets.all(20.0),
        child: new Column(
          children: <Widget>[
            new Padding(
                padding: new EdgeInsets.only(bottom: 10.0),
                child: new Text(
                  "Budget Preview",
                  style: new TextStyle(
                      fontSize: 20.0,
                      color: Colors.white,
                      fontWeight: FontWeight.w500),
                )),
            new DefaultTabController(
                length: 4,
                initialIndex: 2,
                child: new Container(
                  child: new TabBar(
                    indicatorColor: Colors.white,
                    tabs: [
                      new Tab(
                          child: new Text(
                        "Semester",
                        style: new TextStyle(fontSize: 16.0),
                      )),
                      new Tab(
                          child: new Text(
                        "Week",
                        style: new TextStyle(fontSize: 16.0),
                      )),
                      new Tab(
                          child: new Text(
                        "Day",
                        style: new TextStyle(fontSize: 16.0),
                      )),
                      new Tab(icon: new Icon(Icons.not_interested))
                    ],
                  ),
                ))
          ],
        )));
  }
}
