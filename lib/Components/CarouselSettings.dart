import 'package:flutter/material.dart';

class CarouselSettings extends StatelessWidget {
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
                  "Default View",
                  style: new TextStyle(
                      fontSize: 20.0,
                      color: Colors.white,
                      fontWeight: FontWeight.w500),
                )),
            new DefaultTabController(
                length: 2,
                initialIndex: 0,
                child: new Container(
                  child: new TabBar(
                    indicatorColor: Colors.white,
                    tabs: [
                      new Tab(
                          child: new Text(
                        "Bulldog Bucks",
                        style: new TextStyle(fontSize: 16.0),
                      )),
                      new Tab(
                          child: new Text(
                        "Meal Swipes",
                        style: new TextStyle(fontSize: 16.0),
                      ))
                    ],
                  ),
                ))
          ],
        )));
  }
}
