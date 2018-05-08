import 'package:flutter/material.dart';
import 'ListData.dart';
import 'Calender.dart';
import '../Screens/Home/styles.dart';

class ListViewContent extends StatelessWidget {
  final Animation<double> listTileWidth;
  final Animation<Alignment> listSlideAnimation;
  final Animation<EdgeInsets> listSlidePosition;
  ListViewContent({
    this.listSlideAnimation,
    this.listSlidePosition,
    this.listTileWidth,
  });
  @override
  Widget build(BuildContext context) {
    return (new Stack(
      alignment: listSlideAnimation.value,
      children: <Widget>[
        // new Calender(
        //     margin: listSlidePosition.value * 6.5,
        //     startTime: new DateTime.now().subtract(new Duration(days: 1))),
        new ListData(
            margin: listSlidePosition.value * 5.5,
            width: listTileWidth.value,
            type: "Sale",
            title: "\$12.00",
            subtitle: "Aloha Island Grill",
            image: alohaIslandGrillImage),
        new ListData(
            margin: listSlidePosition.value * 4.5,
            width: listTileWidth.value,
            type: "Sale",
            title: "\$4.00",
            subtitle: "Bruchi's CheaseStakes & Subs",
            image: bruchisImage),
        new ListData(
            margin: listSlidePosition.value * 3.5,
            width: listTileWidth.value,
            type: "Sale",
            title: "\$10.40",
            subtitle: "Carls Jr",
            image: carlsJrImage),
        new ListData(
            margin: listSlidePosition.value * 2.5,
            width: listTileWidth.value,
            type: "Sale",
            title: "\$4.10",
            subtitle: "Caruso's",
            image: carusosImage),
        new ListData(
            margin: listSlidePosition.value * 1.5,
            width: listTileWidth.value,
            type: "Sale",
            title: "\$23.20",
            subtitle: "Clarks Fork",
            image: clarksForkImage),
        new ListData(
            margin: listSlidePosition.value * 0.5,
            width: listTileWidth.value,
            type: "Sale",
            title: "\$21.98",
            subtitle: "Domino's Pizza",
            image: dominosImage),
      ],
    ));
  }
}

//For large set of data

// DataListBuilder dataListBuilder = new DataListBuilder();
// var i = dataListBuilder.rowItemList.length + 0.5;
// children: dataListBuilder.rowItemList.map((RowBoxData rowBoxData) {
//   return new ListData(
//     title: rowBoxData.title,
//     subtitle: rowBoxData.subtitle,
//     image: rowBoxData.image,
//     width: listTileWidth.value,
//     margin: listSlidePosition.value * (--i).toDouble(),
//   );
// }).toList(),
