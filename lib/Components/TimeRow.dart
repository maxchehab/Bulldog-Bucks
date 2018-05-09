import "package:flutter/material.dart";

class TimeRow extends StatelessWidget {
  final EdgeInsets margin;
  final double width;
  final DateTime time;

  TimeRow({this.margin, this.width, this.time});

  static final List<String> weekdays = [
    "Monday",
    "Tuesday",
    "Wednesday",
    "Thursday",
    "Friday",
    "Saturday",
    "Sunday"
  ];

  static final List<String> months = [
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December"
  ];

  static String suffix(int number) {
    var j = number % 10, k = number % 100;
    if (j == 1 && k != 11) {
      return number.toString() + "st";
    }
    if (j == 2 && k != 12) {
      return number.toString() + "nd";
    }
    if (j == 3 && k != 13) {
      return number.toString() + "rd";
    }
    return number.toString() + "th";
  }

  static String phrase(DateTime time) {
    DateTime today = new DateTime.now();
    DateTime yesterday = new DateTime.now().subtract(new Duration(days: 1));

    if (isSameDay(time, today)) {
      return "Today";
    } else if (isSameDay(time, yesterday)) {
      return "Yesterday";
    } else if (time.difference(today).inDays < 5) {
      return weekdays[time.weekday - 1];
    } else {
      return weekdays[time.weekday - 1] +
          ", " +
          months[time.month - 1] +
          suffix(time.day) +
          ", " +
          time.year.toString();
    }
  }

  static bool isSameDay(DateTime t1, DateTime t2) {
    return (t1.day == t2.day && t1.month == t2.month && t1.year == t2.year);
  }

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
                phrase(time),
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
