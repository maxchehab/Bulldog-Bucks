import 'package:flutter/material.dart';

class CustomTitle extends StatelessWidget {
  final String title;
  CustomTitle(String title) : this.title = title;
  @override
  Widget build(BuildContext context) {
    return (new Container(
        width: 250.0,
        height: 250.0,
        child: new Center(
            child: new RichText(
          textAlign: TextAlign.center,
          text: new TextSpan(
              text: title,
              style:
                  new TextStyle(fontSize: 38.0, fontWeight: FontWeight.bold)),
        ))));
  }
}
