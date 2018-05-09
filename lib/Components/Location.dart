import 'package:flutter/material.dart';

class Location {
  List<String> keywords;
  DecorationImage image;

  Location(List<String> keywords, String imagePath) {
    this.keywords = keywords;
    this.image = new DecorationImage(
      image: new ExactAssetImage(imagePath),
      fit: BoxFit.fitWidth,
    );
  }

  static Location findLocationFromKeyWords(
      List<Location> locations, String keywords) {
    keywords = keywords.replaceAll(new RegExp("[^A-Za-z\\s]"), "");
    List<String> keys = keywords.toLowerCase().split(' ');

    for (Location location in locations) {
      for (String key in keys) {
        key = key.toLowerCase();
        if (location.keywords.contains(key)) {
          return location;
        }
      }
    }
    debugPrint(keys.toString());
    return null;
  }
}
