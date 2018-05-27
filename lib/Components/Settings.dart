import 'package:flutter/material.dart';
import 'package:flutter/scheduler.dart' show timeDilation;
import 'dart:async';

typedef Future<Null> CloseAnimationCallback(bool);

class Settings extends StatelessWidget {
  Settings({this.size, Key key, this.close, this.controller})
      : scrollAnimation = new Tween(
          begin: -size.height,
          end: 0.0,
        ).animate(
          new CurvedAnimation(parent: controller, curve: Curves.easeIn),
        );

  final Animation<double> controller;
  final CloseAnimationCallback close;
  final Size size;
  final Animation scrollAnimation;
  final List<int> dismissibleList = [0];

  Dismissible _generateDismissible() {
    return new Dismissible(
      direction: DismissDirection.up,
      onDismissed: (direction) {
        dismissibleList.clear();
        dismissibleList.add(0);
        close(true);
      },
      child: new Container(
          height: size.height,
          width: size.width,
          decoration: new BoxDecoration(
            color: const Color.fromRGBO(247, 64, 106, 1.0),
          ),
          child: new Center(
              child: new RichText(
            textAlign: TextAlign.center,
            text: new TextSpan(
                text: 'Settings',
                style:
                    new TextStyle(fontSize: 38.0, fontWeight: FontWeight.bold)),
          ))),
      key: new ObjectKey(null),
    );
  }

  Widget _build(BuildContext context, Widget child) {
    timeDilation = 0.6;
    return (new Transform(
      transform: new Matrix4.translationValues(0.0, scrollAnimation.value, 0.0),
      child: new ListView.builder(
          padding: new EdgeInsets.all(0.0),
          itemCount: dismissibleList.length,
          itemBuilder: (context, index) {
            return _generateDismissible();
          }),
    ));
    // return _generateDismissible();
  }

  @override
  Widget build(BuildContext context) {
    return new AnimatedBuilder(builder: _build, animation: controller);
  }
}
