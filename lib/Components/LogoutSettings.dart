import 'package:flutter/material.dart';
import 'dart:async';

typedef Future<Null> LogoutCallback();

class LogoutSettings extends StatelessWidget {
  LogoutSettings(this.logout);

  final LogoutCallback logout;

  Future<Null> _neverSatisfied(BuildContext context) async {
    return showDialog<Null>(
      context: context,
      barrierDismissible: true,
      builder: (BuildContext context) {
        return new AlertDialog(
          title: new Text('Are you sure you want to leave?'),
          actions: <Widget>[
            new FlatButton(
              child: new Text('Cancel'),
              onPressed: () {
                Navigator.of(context).pop();
              },
            ),
            new Container(
                height: 40.0,
                width: 120.0,
                alignment: FractionalOffset.center,
                decoration: new BoxDecoration(
                  color: Theme.of(context).accentColor,
                  borderRadius:
                      new BorderRadius.all(const Radius.circular(30.0)),
                ),
                child: new InkWell(
                    onTap: () {
                      Navigator.of(context).pop();
                      logout();
                    },
                    child: new Text("Yes, I'm sure.",
                        style: new TextStyle(color: Colors.white))))
          ],
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    return (new Container(
        decoration: new BoxDecoration(
            border: new Border(
                bottom: new BorderSide(width: 0.5, color: Colors.white24))),
        child: new InkWell(
            child: new FlatButton(
          onPressed: () {
            _neverSatisfied(context);
            //logout();
          },
          child: new ListTile(
              contentPadding: new EdgeInsets.all(20.0),
              title: new Text("Logout",
                  style: new TextStyle(
                      fontSize: 20.0,
                      color: Colors.white,
                      fontWeight: FontWeight.w500)),
              leading:
                  new Icon(Icons.exit_to_app, color: Colors.white, size: 26.0)),
        ))));
  }
}
