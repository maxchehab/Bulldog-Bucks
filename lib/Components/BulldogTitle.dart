import 'package:flutter/material.dart';

class BulldogTitle extends StatelessWidget {
  BulldogTitle();
  @override
  Widget build(BuildContext context) {
    return (new Container(
        width: 250.0,
        height: 250.0,
        child: new Center(
            child: new RichText(
          textAlign: TextAlign.center,
          text: new TextSpan(
              text: 'Bulldog Bucks',
              style:
                  new TextStyle(fontSize: 38.0, fontWeight: FontWeight.bold)),
        ))));
  }
}
