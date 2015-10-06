# GeoSearch

GeoSearch is an app for Android that allows a "finder" to find a location set by the "setter" using Google Maps.
The setter will enter the exact coordinates of the location with the desired radius of the area to be explored,
and a map will be created displaying a circle. The circle represents the area to be explored,
or where the "finder" must travel around to find the set location. 
The circle is shifted randomly so that the target location can be anywhere within the circle.
As the user get closer to the target location, there are a few set distances at which the circle
will shrink and change color to indicate that the finder is getting closer to the target location 
and decrease the possible area to explore.

Future Goals:

-Allow coordinate points to be saved and accessed by touching the Saved Searches button on th main screen
  -Currently contains hardcoded coordinates of a geocache in Manlius, NY and Jordan-Hare Stadium for testing
-Improve design of buttons on the main activity
-Choose a better picture for the Custom Search activity
-Enable functionality of the setting button in the top right
