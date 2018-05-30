import 'package:flutter/material.dart';
import 'CustomTitle.dart';
import 'LogoutSettings.dart';
import 'BudgetingSettings.dart';
import 'CarouselSettings.dart';
import 'FreezeSettings.dart';
import 'package:flutter/scheduler.dart' show timeDilation;
import 'dart:async';

typedef Future<Null> CloseAnimationCallback(bool);

class Settings extends StatelessWidget {
  Settings({this.size, Key key, this.close, this.logout, this.controller})
      : scrollAnimation = new Tween(
          begin: -size.height,
          end: 0.0,
        ).animate(
          new CurvedAnimation(parent: controller, curve: Curves.easeIn),
        );

  final Animation<double> controller;
  final CloseAnimationCallback close;
  final LogoutCallback logout;
  final Size size;
  final Animation scrollAnimation;
  final List<int> dismissibleList = [0];

  Dismissible _generateDismissible(BuildContext context) {
    return new Dismissible(
        key: new ObjectKey(null),
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
              color: Theme.of(context).accentColor,
            ),
            child: new Stack(
              children: <Widget>[
                new Column(
                  mainAxisAlignment: MainAxisAlignment.start,
                  children: <Widget>[
                    new Container(
                        decoration: new BoxDecoration(
                            border: new Border(
                          bottom: new BorderSide(
                            width: 0.5,
                            color: Colors.white24,
                          ),
                        )),
                        padding: new EdgeInsets.only(top: 45.0, bottom: 20.0),
                        child: new CustomTitle("Settings")),
                    new CarouselSettings(),
                    new BudgetingSettings(),
                    new FreezeSettings(),
                    new LogoutSettings(logout),
                  ],
                )
              ],
            )));
  }

  Widget _build(BuildContext context, Widget child) {
    timeDilation = 0.6;
    return (new Transform(
      transform: new Matrix4.translationValues(0.0, scrollAnimation.value, 0.0),
      child: new ListView.builder(
          padding: new EdgeInsets.all(0.0),
          itemCount: dismissibleList.length,
          itemBuilder: (context, index) {
            return _generateDismissible(context);
          }),
    ));
  }

  @override
  Widget build(BuildContext context) {
    return new AnimatedBuilder(builder: _build, animation: controller);
  }
}
