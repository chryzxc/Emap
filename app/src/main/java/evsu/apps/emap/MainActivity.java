package evsu.apps.emap;

import android.animation.ValueAnimator;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.core.exceptions.ServicesException;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.OnLocationLayerClickListener;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;
import com.mapbox.mapboxsdk.style.expressions.Expression;
import com.mapbox.mapboxsdk.style.layers.Layer;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mapbox.core.constants.Constants.PRECISION_6;
import static com.mapbox.mapboxsdk.style.layers.Property.NONE;
import static com.mapbox.mapboxsdk.style.layers.Property.VISIBLE;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconSize;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.visibility;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback,LocationEngineListener, PermissionsListener,View.OnClickListener,MapboxMap.OnFlingListener,MapboxMap.OnMapLongClickListener, MapboxMap.OnMapClickListener {

    private MapView mapView;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_events:
                    Intent eventsIntent = new Intent(MainActivity.this, Events.class);
                    startActivityForResult(eventsIntent, 3);
                    //  item.setCheckable(true);
                    homeClear();
                    cancel();
                    return false;
                //        return true;
                case R.id.navigation_home:
                    //item.setCheckable(true);
                    removeLayers();
                    homeClear();
                    cancel();
                    return false;
                //      return true;
                case R.id.navigation_directions:
                    if (list.size() == 0) {
                        buildAlertMessageNoOfflineDatabase();
                    } else {
                        textDistance.setText("0");
                        initializeLocationEngine();
                        removeLayers();
                        homeClear();
                        Intent directionIntent = new Intent(MainActivity.this, Direction.class);
                        startActivityForResult(directionIntent, 0);
                        //      item.setCheckable(true);
                    }
                    cancel();
                    return false;
                //    return true;
            }
            return false;
        }
    };
    static Integer roomCode = 0;
    public Integer getCode = 0;
    public BottomNavigationView navigation;
    private TextView textDistance, walkingTime, runningTime, walkingTime1, runningTime1, textDistance1, buildingText, originText, destinationText, roomText, miText, loadingText, latlngText, clickBuilding;
    private String thisName;
    private Double thisLat, thisLong, thisZoom, thisBearing, thisTilt, clickLat, clickLong, currentZoom;
    public Polyline polyline;
    private MapboxMap map;
    private PermissionsManager permissionsManager;
    private LocationLayerPlugin locationPlugin;
    private LocationEngine locationEngine;
    public static Location originLocation;
    static ConstraintLayout d;
    private ConstraintLayout buildingPopup, directionPopup, i, locate, loadingPopup, visibilityPopup, visibility, longclickPopup, lcClick, zoom, zoomIn, zoomOut, zoomOutMap, dummyBuildingPopup, clickPopup;
    private ImageView buildingView, image1, image2, toggleVisibility, clickImage, copyText;
    private Marker destinationMarker;
    private LatLng originCoord, destinationCoord;
    private Marker buildingLocationMarker, buildingMarker;
    static Double locationlatitude, locationlongitude, currentlatitude, currentlongitude;
    Double distance, dLat, dLong;
    private Point originPosition, destinationPosition, directionsStartingpoint, directionsDestinationpoint, searchDirectionsStartingPoint, searchDirectionsDestinationPoint, mapLongClickPoint;
    private DirectionsRoute currentRoute;
    private static final String TAG = "MainActivity";
    private NavigationMapRoute navigationMapRoute;
    private ImageButton targetButton, northButton, gpsButton;
    private MapboxDirections client;
    public final static int MILLISECONDS_PER_SECOND = 1000;
    public final static int MINUTE = 60 * MILLISECONDS_PER_SECOND;
    public static String building, room, clickimageName;
    ConnectionDetector cd;
    Location lastLocation;
    CountDownTimer cTimer;
    Marker locationmarker, longclickmarker;
    ProgressDialog prgDialog;
    DatabaseHandler controller = new DatabaseHandler(this);
    HashMap<String, String> queryValues;
    static Integer dbUpdate = 0;
    static Integer numofUpdate;
    List<String> list;
    Integer selected = 0;


    // panning
    private static final LatLngBounds EVSU_CAMERA_BOUNDS = new LatLngBounds.Builder()
            .include(new LatLng(11.240554, 124.996739))
            .include(new LatLng(11.238012, 124.998572))
            .build();

    private static final LatLngBounds EVSU_BOUNDS = new LatLngBounds.Builder()
            .include(new LatLng(11.241154, 124.996955))
            .include(new LatLng(11.237912, 124.995521))
            .include(new LatLng(11.237656, 124.999006))
            .include(new LatLng(11.240343, 124.998815))
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar.setTitleTextColor(getResources().getColor(R.color.colorDarkest));
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(1).setCheckable(false).setChecked(true);

        Mapbox.getInstance(this, getString(R.string.access_token));
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        cd = new ConnectionDetector(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        //building popup
        buildingPopup = findViewById(R.id.buildingPopup);
        buildingText = findViewById(R.id.buildingText);
        roomText = findViewById(R.id.roomText);
        buildingView = findViewById(R.id.buildingView);
        d = findViewById(R.id.d);
        i = findViewById(R.id.i);
        locate = findViewById(R.id.locate);
        image2 = findViewById(R.id.image2);
        miText = findViewById(R.id.miText);

        // direction popup
        directionPopup = findViewById(R.id.directionPopup);
        textDistance = findViewById(R.id.textDistance);
        textDistance.setText("0");
        walkingTime = findViewById(R.id.walkingTime);
        runningTime = findViewById(R.id.runningTime);
        textDistance1 = findViewById(R.id.textDistance1);
        walkingTime1 = findViewById(R.id.walkingTime1);
        runningTime1 = findViewById(R.id.runningTime1);
        originText = findViewById(R.id.originText);
        destinationText = findViewById(R.id.destinationText);


        // location button
        targetButton = findViewById(R.id.targetButton);
        northButton = findViewById(R.id.northButton);
        gpsButton = findViewById(R.id.gpsButton);

        //loading popup

        loadingPopup = findViewById(R.id.loadingPopup);
        loadingText = findViewById(R.id.loadingText);

        //visibility Popup
        visibilityPopup = findViewById(R.id.visibilityPopup);
        visibility = findViewById(R.id.visibility);
        toggleVisibility = findViewById(R.id.toggleVisibility);

        // longclick Popup
        longclickPopup = findViewById(R.id.longclickPopup);
        lcClick = findViewById(R.id.lcClick);
        latlngText = findViewById(R.id.latlngText);
        copyText = findViewById(R.id.copyText);

        // zoom Popup
        zoom = findViewById(R.id.zoom);
        zoomIn = findViewById(R.id.zoomIn);
        zoomOut = findViewById(R.id.zoomOut);
        zoomOutMap = findViewById(R.id.zoomOutMap);
        dummyBuildingPopup = findViewById(R.id.dummyBuildingPopup);

        // click Popup
        clickPopup = findViewById(R.id.clickPopup);
        clickBuilding = findViewById(R.id.clickBuilding);
        clickImage = findViewById(R.id.clickImage);


        // onclick listener
        navigationView.setNavigationItemSelectedListener(this);
        targetButton.setOnClickListener(this);
        northButton.setOnClickListener(this);
        gpsButton.setOnClickListener(this);
        d.setOnClickListener(this);
        i.setOnClickListener(this);
        visibility.setOnClickListener(this);
        lcClick.setOnClickListener(this);
        zoomIn.setOnClickListener(this);
        zoomOut.setOnClickListener(this);
        zoomOutMap.setOnClickListener(this);
        copyText.setOnClickListener(this);





        prgDialog = new ProgressDialog(MainActivity.this);
        numofUpdate = controller.getUpdate();
        connect();
        list = controller.getBuildingNames();

        if (getIntent().getExtras() != null) {
            buildAlertDatabaseUpdateAvailable();
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            MainActivity.this.finish();
            moveTaskToBack(true);
            ///    super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://design6500.com"));
            startActivity(browserIntent);

            cancel();
            return true;
        } else if (id == R.id.action_search) {
            if (list.size() == 0) {
                buildAlertMessageNoOfflineDatabase();
            } else {
                textDistance.setText("0");
                textDistance1.setText("");
                walkingTime.setText("");
                walkingTime1.setText("");
                runningTime.setText("");
                runningTime1.setText("");
                originText.setText("");
                destinationText.setText("");
                if (roomText.getVisibility() == View.VISIBLE) {
                    roomText.setVisibility(View.INVISIBLE);
                }
                initializeLocationEngine();
                homeClear();
                removeLayers();
                cancel();
                Intent sIntent = new Intent(MainActivity.this, Search.class);
                startActivityForResult(sIntent, 1);
            }
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_help) {
            Intent helpIntent = new Intent(MainActivity.this, Help.class);
            startActivityForResult(helpIntent, 11);

            cancel();
        } else if (id == R.id.nav_about) {
            Intent eventsIntent = new Intent(MainActivity.this, About.class);
            startActivityForResult(eventsIntent, 9);

            cancel();
        } else if (id == R.id.nav_feedback) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://design6500.com/feedback"));
            startActivity(browserIntent);
            cancel();
        } else if (id == R.id.nav_update) {
            NavigationLauncherOptions options = NavigationLauncherOptions.builder().directionsRoute(currentRoute).shouldSimulateRoute(true).build();
            NavigationLauncher.startNavigation(this, options);

        //    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://design6500.com/download"));
       //     startActivity(browserIntent);

            cancel();
        } else if (id == R.id.nav_database) {
            checkDatabaseUpdate();
            cancel();
        } else if (id == R.id.nav_exit) {
            cancel();
            MainActivity.this.finish();
            moveTaskToBack(true);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;


    }


    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        map = mapboxMap;
        enableLocationPlugin();
        map.setMinZoomPreference(16.5);
        map.setMaxZoomPreference(20);
        CameraPosition position = new CameraPosition.Builder().target(new LatLng(11.240103, 124.997382)).zoom(17).tilt(60).bearing(180).build();
        map.setCameraPosition(position);
        //   map.setStyleUrl("mapbox://styles/chryzxc/cjou70fxh5cwm2rn09outswx2");
        //    map.setLatLngBoundsForCameraTarget(EVSU_BOUNDS);
        //    map.getUiSettings().setDeselectMarkersOnTap(false);
        //   map.selectMarker(buildingMarker);
        //    showBoundsArea();
        //    map.setStyleUrl("mapbox://styles/chryzxc/cjn47pu47064y2rmto1bi5cdr");

        addTextLayer();

        map.getUiSettings().setCompassMargins(30, 40, 0, 0);
        map.getUiSettings().setCompassGravity(500);
        map.getUiSettings().setLogoEnabled(false);
        map.getUiSettings().setAttributionEnabled(false);
        map.addOnFlingListener(this);
        map.addOnMapLongClickListener(this);
        map.addOnMapClickListener(this);

        originCoord = new LatLng(originLocation.getLatitude(), originLocation.getLongitude());


    }


    private void locationButton() {

        if (directionPopup.getVisibility() == View.VISIBLE) {
            if (targetButton.getVisibility() == View.VISIBLE) {
                targetButton.setVisibility(View.GONE);
            }
            if (northButton.getVisibility() == View.VISIBLE) {
                northButton.setVisibility(View.GONE);
            }
            if (gpsButton.getVisibility() == View.VISIBLE) {
                gpsButton.setVisibility(View.GONE);
            }
        } else {
            if (locationPlugin.getCameraMode() != CameraMode.TRACKING_COMPASS) {
                targetButton.setVisibility(View.VISIBLE);
                if (northButton.getVisibility() == View.VISIBLE) {
                    northButton.setVisibility(View.GONE);
                }
                if (gpsButton.getVisibility() == View.VISIBLE) {
                    gpsButton.setVisibility(View.GONE);
                }
            }
            if (locationPlugin.getCameraMode() != CameraMode.TRACKING_GPS_NORTH) {
                targetButton.setVisibility(View.VISIBLE);
                if (northButton.getVisibility() == View.VISIBLE) {
                    northButton.setVisibility(View.GONE);
                }
                if (gpsButton.getVisibility() == View.VISIBLE) {
                    gpsButton.setVisibility(View.GONE);
                }
            }
        }

        if (buildingPopup.getVisibility() == View.VISIBLE) {
            if (targetButton.getVisibility() == View.VISIBLE) {
                targetButton.setVisibility(View.GONE);
            }
            if (northButton.getVisibility() == View.VISIBLE) {
                northButton.setVisibility(View.GONE);
            }
            if (gpsButton.getVisibility() == View.VISIBLE) {
                gpsButton.setVisibility(View.GONE);
            }
        } else {
            if (locationPlugin.getCameraMode() != CameraMode.TRACKING_COMPASS) {
                targetButton.setVisibility(View.VISIBLE);
                if (northButton.getVisibility() == View.VISIBLE) {
                    northButton.setVisibility(View.GONE);
                }
                if (gpsButton.getVisibility() == View.VISIBLE) {
                    gpsButton.setVisibility(View.GONE);
                }
            }
            if (locationPlugin.getCameraMode() != CameraMode.TRACKING_GPS_NORTH) {
                targetButton.setVisibility(View.VISIBLE);
                if (northButton.getVisibility() == View.VISIBLE) {
                    northButton.setVisibility(View.GONE);
                }
                if (gpsButton.getVisibility() == View.VISIBLE) {
                    gpsButton.setVisibility(View.GONE);
                }
            }
        }


    }


    private void getRoute(Point origin, Point destination) {
        NavigationRoute.builder(this)
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .profile(DirectionsCriteria.PROFILE_WALKING)
                .alternatives(true)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        currentRoute = response.body().routes().get(0);
                        // Draw the route on the map
                        if (navigationMapRoute != null) {
                            navigationMapRoute.removeRoute();
                        } else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, map, R.style.NavigationMapRoute);
                        }

                        navigationMapRoute.addRoute(currentRoute);

                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Log.e(TAG, "Error: " + throwable.getMessage());
                    }
                });
    }


    private void showDistanceandDuration(Point origin, Point destination) throws ServicesException {
        client = MapboxDirections.builder()
                .origin(origin)
                .destination(destination)
                .profile(DirectionsCriteria.PROFILE_WALKING)
                .accessToken(Mapbox.getAccessToken())
                .steps(true)
                .build();
        client.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                System.out.println(call.request().url().toString());
                distance = response.body().routes().get(0).distance();
                Integer mpk = 1000;
                Double wmps = 1.38;
                Double rmps = 3.2;
                Integer minute = 60;
                Double wduration = distance / wmps;
                Double rduration = distance / rmps;
                if (distance < mpk) {
                    if (distance > 1) {
                        textDistance.setText(String.format("%.0f", distance));
                        textDistance1.setText("meters");
                    }
                    if (distance == 1) {
                        textDistance.setText(String.format("%.0f", distance));
                        textDistance1.setText("meter");
                    }
                } else {
                    Double value = distance / mpk;
                    if (value == 1) {
                        textDistance.setText(value.toString());
                        textDistance1.setText("kilometer");
                    }
                    if (value > 1) {
                        textDistance.setText(value.toString());
                        textDistance1.setText("kilometers");
                    }
                }


                // WALKING
                if (wduration < 60) {
                    if (wduration > 1) {
                        walkingTime.setText(String.format("%.0f", wduration));
                        walkingTime1.setText("seconds");

                    }
                    if (wduration == 1) {
                        walkingTime.setText(String.format("%.0f", wduration));
                        walkingTime1.setText("second");
                    }
                } else {
                    Integer wduration1 = Integer.valueOf(wduration.intValue());
                    Double walkingtime = wduration / minute;
                    Integer walkingtime1 = Integer.valueOf(walkingtime.intValue());
                    Integer x = walkingtime1 * minute;
                    Integer walkingseconds = wduration1 - x;


                    if (walkingtime1 >= 2) {
                        walkingTime.setText(walkingtime1.toString());
                        walkingTime1.setText("minutes and " + walkingseconds + " seconds");
                    }
                    if (walkingtime1 < 2) {
                        walkingTime.setText(walkingtime1.toString());
                        walkingTime1.setText("minute and " + walkingseconds + " seconds");
                    }

                }

                // RUNNING

                if (rduration < 60) {
                    if (wduration > 1) {
                        runningTime.setText(String.format("%.0f", rduration));
                        runningTime1.setText("seconds");
                    }
                    if (rduration == 1) {
                        runningTime.setText(String.format("%.0f", rduration));
                        runningTime1.setText("second");
                    }
                } else {
                    Integer rduration1 = Integer.valueOf(rduration.intValue());
                    Double runningtime = rduration / minute;
                    Integer runningtime1 = Integer.valueOf(runningtime.intValue());
                    Integer x = runningtime1 * minute;
                    Integer runningseconds = rduration1 - x;


                    if (runningtime >= 2) {
                        runningTime.setText(runningtime1.toString());
                        runningTime1.setText("minutes and " + runningseconds + " seconds");
                    }
                    if (runningtime < 2) {
                        runningTime.setText(runningtime1.toString());
                        runningTime1.setText("minute and " + runningseconds + " seconds");
                    }

                }


            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {

            }
        });
    }


    private void showDirection(Point origin, Point destination) throws ServicesException {
        client = MapboxDirections.builder()
                .origin(origin)
                .destination(destination)
                .profile(DirectionsCriteria.PROFILE_WALKING)
                .accessToken(Mapbox.getAccessToken())
                .alternatives(true)
                .steps(true)
                .build();
        client.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                System.out.println(call.request().url().toString());
                currentRoute = response.body().routes().get(0);
                if (navigationMapRoute != null) {
                    navigationMapRoute.removeRoute();
                }
                drawRoute(currentRoute);
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {
            }
        });
    }


    private void drawRoute(DirectionsRoute route) {
        LineString lineString = LineString.fromPolyline(route.geometry(), PRECISION_6);
        List<Point> coordinates = lineString.coordinates();
        LatLng[] points = new LatLng[coordinates.size()];
        for (int i = 0; i < coordinates.size(); i++) {
            points[i] = new LatLng(coordinates.get(i).latitude(), coordinates.get(i).longitude());
        }
        if (polyline != null) {
            polyline.remove();
            polyline = null;
        }

        polyline = map.addPolyline(new PolylineOptions()
                .add(points).color(Color.parseColor("#FFFF4040")).width(3));

    }

    private void homeClear() {

        if (clickPopup.getVisibility() == View.VISIBLE) {
            clickPopup.setVisibility(View.GONE);
        }


        clickable();
        if (longclickPopup.getVisibility() == View.VISIBLE) {
            longclickPopup.setVisibility(View.GONE);
        }

        if (longclickmarker != null) {
            longclickmarker.remove();
        }

        if (loadingPopup.getVisibility() == View.VISIBLE) {
            loadingPopup.setVisibility(View.GONE);
        }

        if (locate.getVisibility() == View.GONE) {
            locate.setVisibility(View.VISIBLE);
        }
        if (navigationMapRoute != null) {
            navigationMapRoute.removeRoute();
        }
        if (polyline != null) {
            polyline.remove();
            polyline = null;
        }
        if (buildingLocationMarker != null) {
            map.removeMarker(buildingLocationMarker);
        }
        if (directionPopup.getVisibility() == View.VISIBLE) {
            directionPopup.setVisibility(View.GONE);
        }
        if (buildingPopup.getVisibility() == View.VISIBLE) {
            buildingPopup.setVisibility(View.GONE);
            dummyBuildingPopup.setVisibility(View.GONE);
            targetButton.setVisibility(View.VISIBLE);
        }
        if (targetButton.getVisibility() == View.GONE) {
            targetButton.setVisibility(View.VISIBLE);
        }
        navigation.getMenu().getItem(1).setCheckable(false);


    }

    private void removeMarkers() {
        if (buildingLocationMarker != null) {
            map.removeMarker(buildingLocationMarker);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            locationButton();
            homeClear();
            // navigation.getMenu().getItem(2).setCheckable(false);
            if (Direction.code == 1) {
                directionResult();

            }
        }

        if (requestCode == 1) {
            locationButton();


            homeClear();


            if (Search.code == 1) {
                searchResult();
            }

            if (Search.code == 2) {
                if (ListViewAdapter.code == 1) {
                    roomIntent();
                }
            }


        }

        if (requestCode == 2) {
            locationButton();
            homeClear();
            if (Building.pass == 0) {
                homeClear();
                searchResult();
            }
            if (Building.pass == 1) {
                homeClear();
                searchGetDirection();
            }
            if (Building.pass == 2) {
                searchResult();
            }

        }
        if (requestCode == 3) {
            locationButton();
            //   navigation.getMenu().getItem(0).setCheckable(false);
            homeClear();
        }
    }

    public void addTextLayer() {

        Integer b = controller.getBuildingCount();
        List<String> bName = controller.getbName();
        List<Double> bLat = controller.getbLat();
        List<Double> bLon = controller.getbLon();
        Integer imgsize = 10;
        Double iconsize = 0.3;
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_black_marker);
        map.addImage("image", icon);
        List<Feature> featurelist = new ArrayList<>();
        for (int x = 0; x < b; x++) {

            Point point = Point.fromLngLat(bLon.get(x), bLat.get(x));
            featurelist.add(Feature.fromGeometry(point, null, bName.get(x)));
        }

        FeatureCollection featureCollection = FeatureCollection.fromFeatures(featurelist);
        GeoJsonSource geoJsonSource = new GeoJsonSource("source", featureCollection);
        map.addSource(geoJsonSource);


        SymbolLayer symbolLayer = new SymbolLayer("layer", "source")
                .withProperties(PropertyFactory.iconImage("image"),
                        PropertyFactory.textAnchor("bottom"),
                        PropertyFactory.textField(Expression.id()),
                        PropertyFactory.textColor(Color.BLACK),
                        PropertyFactory.textKeepUpright(false),
                        PropertyFactory.textHaloBlur(iconsize.floatValue()),
                        PropertyFactory.textAllowOverlap(false),
                        PropertyFactory.textIgnorePlacement(false),
                        PropertyFactory.iconAnchor("top"),
                        PropertyFactory.iconSize(0.4f),
                        PropertyFactory.circleColor(Color.BLACK),
                        //           PropertyFactory.iconOffset(new Float[]{0f,-9f}),
                        PropertyFactory.iconAllowOverlap(false),
                        ///     PropertyFactory.textOpacity(0.5f),
                        PropertyFactory.textSize(imgsize.floatValue()));
        symbolLayer.setProperties(visibility(VISIBLE));
        map.addLayer(symbolLayer);





    }


    public void roomIntent() {
        IconFactory iconFactory = IconFactory.getInstance(MainActivity.this);
        Icon icon = iconFactory.fromResource(R.drawable.map_marker_dark);

        thisName = ListViewAdapter.building;
        thisLat = controller.getbuildinglat();
        thisLong = controller.getbuildinglon();
        String floor;
        String image = controller.getimage();

        locationPlugin.setCameraMode(CameraMode.NONE);

        CameraPosition moveCamera = new CameraPosition.Builder().target(new LatLng(thisLat, thisLong)).zoom(18).bearing(40).tilt(90).build();
        map.moveCamera(CameraUpdateFactory.newCameraPosition(moveCamera));
        buildingPopup.setVisibility(View.VISIBLE);
        dummyBuildingPopup.setVisibility(View.VISIBLE);
        buildingText.setText(thisName);
        if (roomText.getVisibility() == View.INVISIBLE) {
            roomText.setVisibility(View.VISIBLE);
        }
        roomText.setText(ListViewAdapter.room);
        if (image != null) {
            Picasso.get().load("http://design6500.com/assets/public/img/building/" + image).placeholder(R.drawable.no_preview).error(R.drawable.no_preview).into(buildingView, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {


                }
            });
            // buildingView.setImageBitmap(image);
        } else {
            buildingView.setImageDrawable(getResources().getDrawable(R.drawable.no_preview));
        }
        removeMarkers();
        Bitmap iconmarker = BitmapFactory.decodeResource(getResources(), R.drawable.map_marker_dark);
        map.addImage("markericon", iconmarker);
        removeLayers();
        map.addSource(new GeoJsonSource("marker", FeatureCollection.fromFeature(Feature.fromGeometry(Point.fromLngLat(thisLong, thisLat)))));
        map.addLayer(new SymbolLayer("markerlayer", "marker").withProperties(PropertyFactory.iconImage("markericon"), iconSize(1f), PropertyFactory.iconOffset(new Float[]{-10f, -9f}), PropertyFactory.iconAllowOverlap(false), PropertyFactory.iconIgnorePlacement(false)));
        //   buildingLocationMarker = map.addMarker(new MarkerOptions().position(new LatLng(thisLat, thisLong)).icon(icon));
        locate.setVisibility(View.GONE);
        unclickable();
        miText.setText("View");
        image2.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_vr));

    }


    public void directionResult() {

        directionsStartingpoint = Direction.dstartingPoint;
        directionsDestinationpoint = Direction.ddestinationPoint;
        String originName = Direction.dstartingPointName;
        String destinationName = Direction.ddestinationPointName;
        dLat = Direction.ddestinationPoint.latitude(); // ==
        dLong = Direction.ddestinationPoint.longitude();  // ==
        removeMarkers();
        getRoute(directionsStartingpoint, directionsDestinationpoint);
        showDistanceandDuration(directionsStartingpoint, directionsDestinationpoint);
        locationPlugin.setCameraMode(CameraMode.NONE);
        originText.setText(originName);
        destinationText.setText(destinationName);

        locate.setVisibility(View.GONE);
        loadingPopup.setVisibility(View.VISIBLE);
        loadingText.setText("Finding route from " + originName + " to " + destinationName);
        unclickable();
        timer();
        getCode = 2;

    }

    public void searchResult() {
        IconFactory iconFactory = IconFactory.getInstance(MainActivity.this);
        Icon icon = iconFactory.fromResource(R.drawable.map_marker_dark);
        thisName = Search.searchNameTab;
        thisLat = Search.searchLat;
        thisLong = Search.searchLong;
        //   thisZoom = Search.searchZoom;
        //   thisBearing = Search.searchBearing;
        //   thisTilt = Search.searchTilt;
        CameraPosition moveCamera = new CameraPosition.Builder().target(new LatLng(thisLat, thisLong)).zoom(18).bearing(55).tilt(90).build();
        map.moveCamera(CameraUpdateFactory.newCameraPosition(moveCamera));
        buildingPopup.setVisibility(View.VISIBLE);
        dummyBuildingPopup.setVisibility(View.VISIBLE);
        buildingText.setText(thisName);
        if (Search.searchImage != null) {
            Picasso.get().load("http://design6500.com/assets/public/img/building/" + Search.searchImage).placeholder(R.drawable.no_preview).error(R.drawable.no_preview).into(buildingView, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {


                }
            });
            //    buildingView.setImageBitmap(Search.searchImage);
        } else {
            buildingView.setImageDrawable(getResources().getDrawable(R.drawable.no_preview));
        }
        removeMarkers();
        Bitmap iconmarker = BitmapFactory.decodeResource(getResources(), R.drawable.map_marker_dark);
        map.addImage("markericon", iconmarker);
        removeLayers();
        map.addSource(new GeoJsonSource("marker", FeatureCollection.fromFeature(Feature.fromGeometry(Point.fromLngLat(thisLong, thisLat)))));
        map.addLayer(new SymbolLayer("markerlayer", "marker").withProperties(PropertyFactory.iconImage("markericon"), iconSize(1f), PropertyFactory.iconOffset(new Float[]{-10f, -9f}), PropertyFactory.iconAllowOverlap(false), PropertyFactory.iconIgnorePlacement(false)));
        //  buildingLocationMarker = map.addMarker(new MarkerOptions().position(new LatLng(thisLat, thisLong)).icon(icon));
        locate.setVisibility(View.GONE);
        unclickable();
        miText.setText("More info");
        image2.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_vi));
        locationPlugin.setCameraMode(CameraMode.NONE);

    }


    public void searchGetDirection() {


        buildingPopup.setVisibility(View.GONE);
        dummyBuildingPopup.setVisibility(View.GONE);

        String originName, destinationName;
        searchDirectionsDestinationPoint = Point.fromLngLat(thisLong, thisLat);
        if (originLocation != null) {
            Marker locationmarker = map.addMarker(new MarkerOptions().position(new LatLng
                    (locationlatitude, locationlongitude)));
            if (EVSU_BOUNDS.contains(locationmarker.getPosition())) {
                locationmarker.remove();
                originName = "Current location";
                searchDirectionsStartingPoint = Point.fromLngLat(locationlongitude, locationlatitude);
            } else {
                locationmarker.remove();
                originName = "EVSU main gate";
                searchDirectionsStartingPoint = Point.fromLngLat(124.997556, 11.240392);
            }
        } else {
            originName = "EVSU main gate";
            searchDirectionsStartingPoint = Point.fromLngLat(124.997556, 11.240392);
        }
        destinationName = thisName;
        getRoute(searchDirectionsStartingPoint, searchDirectionsDestinationPoint);
        showDistanceandDuration(searchDirectionsStartingPoint, searchDirectionsDestinationPoint);
        originText.setText(originName);
        destinationText.setText(destinationName);
        locate.setVisibility(View.GONE);
        loadingPopup.setVisibility(View.VISIBLE);
        loadingText.setText("Finding route to " + thisName);
        //      directionPopup.setVisibility(View.VISIBLE);

        //     CameraPosition moveCamera = new CameraPosition.Builder().target(new LatLng(11.2393747, 124.9974016)).zoom(17).bearing(170).tilt(90).build(); // ==
        //      map.animateCamera(CameraUpdateFactory.newCameraPosition(moveCamera));
        //      removeMarkers();
        //      buildingLocationMarker = map.addMarker(new MarkerOptions().position(new LatLng(thisLat, thisLong)).icon(icon));
        unclickable();
        timer();
        getCode = 1;

    }

    public void lcGetDirection() {
        longclickPopup.setVisibility(View.GONE);
        textDistance.setText("0");
        String originName, destinationName;
        searchDirectionsDestinationPoint = mapLongClickPoint;
        if (originLocation != null) {
            Marker locationmarker = map.addMarker(new MarkerOptions().position(new LatLng
                    (locationlatitude, locationlongitude)));
            if (EVSU_BOUNDS.contains(locationmarker.getPosition())) {
                locationmarker.remove();
                originName = "Current location";
                searchDirectionsStartingPoint = Point.fromLngLat(locationlongitude, locationlatitude);
            } else {
                locationmarker.remove();
                originName = "EVSU main gate";
                searchDirectionsStartingPoint = Point.fromLngLat(124.997556, 11.240392);
            }
        } else {
            originName = "EVSU main gate";
            searchDirectionsStartingPoint = Point.fromLngLat(124.997556, 11.240392);
        }
        destinationName = latlngText.getText().toString();
        getRoute(searchDirectionsStartingPoint, searchDirectionsDestinationPoint);
        showDistanceandDuration(searchDirectionsStartingPoint, searchDirectionsDestinationPoint);
        originText.setText(originName);
        destinationText.setText(destinationName);
        locate.setVisibility(View.GONE);
        loadingPopup.setVisibility(View.VISIBLE);
        loadingText.setText("Please wait...");
        //      directionPopup.setVisibility(View.VISIBLE);
        //     CameraPosition moveCamera = new CameraPosition.Builder().target(new LatLng(11.2393747, 124.9974016)).zoom(17).bearing(170).tilt(90).build(); // ==
        //      map.animateCamera(CameraUpdateFactory.newCameraPosition(moveCamera));
        //      removeMarkers();
        //      buildingLocationMarker = map.addMarker(new MarkerOptions().position(new LatLng(thisLat, thisLong)).icon(icon));
        unclickable();
        timer();
        getCode = 0;


    }

    public void clickable() {
        if (!targetButton.isClickable()) {
            targetButton.setClickable(true);
            northButton.setClickable(true);
            gpsButton.setClickable(true);
        }
    }

    public void unclickable() {
        if (targetButton.isClickable()) {
            targetButton.setClickable(false);
            northButton.setClickable(false);
            gpsButton.setClickable(false);
        }
    }

    // DATABASE


    public void connect() {

        // BroadCase Receiver Intent Object
        Intent alarmIntent = new Intent(MainActivity.this, BroadcastReceiverService.class);
        //   alarmIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //  Bundle bundle = new Bundle();
        //  bundle.putString("zz","zz");
        //  alarmIntent.putExtras(bundle);

        // Pending Intent Object
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Alarm Manager Object

        AlarmManager alarmManager = (AlarmManager) MainActivity.this.getSystemService(MainActivity.ALARM_SERVICE);
        // Alarm Manager calls BroadCast for every Ten seconds (10 * 1000), BroadCase further calls service to check if new records are inserted in
        // Remote MySQL DB
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + 5000, 1 * 1000, pendingIntent);

    }


    public void checkDatabaseUpdate() {
        prgDialog.setMessage("Checking for new updates. Please wait...");
        prgDialog.setCancelable(false);
        //   Integer up = controller.getUpdate();
        // Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        // Http Request Params Object
        RequestParams params = new RequestParams();
        // Show ProgressBar
        prgDialog.show();
        // Integer numvalue = numofUpdate;


        client.post("http://design6500.com/building/get_update", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String responseU) {
                try {
                    // Create JSON object out of the response sent by getdbrowcount.php
                    JSONArray array = new JSONArray(responseU);
                    JSONObject obj = array.getJSONObject(0);
                    System.out.println(obj.get("value"));


                    // If the count value is not zero, call MyService to display notification
                    if (obj.getInt("value") != numofUpdate) {
                        prgDialog.setMessage("Preparing for sync...");
                        dbUpdate = obj.getInt("value");
                        syncSQLiteMySqlBuilding();
                    } else {
                        prgDialog.hide();
                        Toast.makeText(MainActivity.this, "Database is up to date", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

            // When error occured
            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                // TODO Auto-generated method stub
                // Hide ProgressBar

                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! Device might not be connected to Internet",
                            Toast.LENGTH_LONG).show();
                }
                prgDialog.hide();
            }
        });
    }


    public void syncSQLiteMySqlBuilding() {
        // prgDialogB = new ProgressDialog(MainActivity.this);
        prgDialog.setMessage("Transferring building data. Please wait...");
        // Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        // Http Request Params Object
        RequestParams params = new RequestParams();
        // Show ProgressBar

        client.post("http://design6500.com/building/view", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String responseB) {
                //    prgDialogB.hide();
                controller.delete();
                controller.create();
                updateSQLiteBuilding(responseB);
            }

            // When error occured
            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                // TODO Auto-generated method stub
                // Hide ProgressBar
                //    prgDialog.hide();
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! Device might not be connected to Internet",
                            Toast.LENGTH_LONG).show();
                }
                syncSQLiteMySqlRooms();
            }
        });
    }

    public void updateSQLiteBuilding(String responseB) {
        //     ArrayList<HashMap<String, String>> usersynclist;
        //    usersynclist = new ArrayList<HashMap<String, String>>();
        Gson gson = new GsonBuilder().create();
        try {
            JSONArray arr = new JSONArray(responseB);
            System.out.println(arr.length());
            if (arr.length() != 0) {

                for (int i = 0; i < arr.length(); i++) {

                    //     JSONObject ob1 = arr.getJSONObject(i);
                    JSONObject obj = (JSONObject) arr.get(i);
                    System.out.println(obj.get("id"));
                    System.out.println(obj.get("name"));
                    System.out.println(obj.get("latitude"));
                    System.out.println(obj.get("longitude"));
                    System.out.println(obj.get("img"));
                    queryValues = new HashMap<String, String>();
                    queryValues.put("id", obj.get("id").toString());
                    queryValues.put("building", obj.get("name").toString());
                    queryValues.put("latitude", obj.get("latitude").toString());
                    queryValues.put("longitude", obj.get("longitude").toString());
                    queryValues.put("image", obj.get("img").toString());
                    queryValues.put("description", obj.get("description").toString());

                    Picasso.get().load("http://design6500.com/assets/public/img/building/" + obj.get("img"));
                    controller.insertBuilding(queryValues);
                }

                syncSQLiteMySqlRooms();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void syncSQLiteMySqlRooms() {

        prgDialog.setMessage("Transferring room data. Please wait...");
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        //   prgDialogR.show();
        client.post("http://design6500.com/building/view_rooms", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String responseR) {
                prgDialog.hide();
                updateSQLiteRoom(responseR);
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content) {
                // TODO Auto-generated method stub
                prgDialog.hide();
                if (statusCode == 404) {
                    Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                } else if (statusCode == 500) {
                    Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unexpected Error occcured! Device might not be connected to Internet",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void updateSQLiteRoom(String response) {

        Gson gson = new GsonBuilder().create();
        try {

            JSONArray arr = new JSONArray(response);
            System.out.println(arr.length());

            if (arr.length() != 0) {
                for (int i = 0; i < arr.length(); i++) {
                    //     JSONObject ob1 = arr.getJSONObject(i);
                    JSONObject obj = (JSONObject) arr.get(i);
                    System.out.println(obj.get("id"));
                    System.out.println(obj.get("name"));
                    System.out.println(obj.get("building_name"));
                    System.out.println(obj.get("img"));
                    System.out.println(obj.get("floor"));
                    queryValues = new HashMap<String, String>();
                    queryValues.put("id", obj.get("id").toString());
                    queryValues.put("building", obj.get("building_name").toString());
                    queryValues.put("room", obj.get("name").toString());
                    queryValues.put("floor", obj.get("floor").toString());
                    queryValues.put("image", obj.get("img").toString());
                    controller.insertRoom(queryValues);
                }
                //    queryValues = new HashMap<String, String>();
                //  queryValues.put("db",dbUpdate.toString());
                controller.updateDb();
                reloadActivity();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }




    public void reloadActivity() {
        Intent objIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(objIntent);
    }

    public void getCurrentZoomLevel() {
        currentZoom = map.getCameraPosition().zoom;
    }


    // TIME

    public void timer() {
        cTimer = new CountDownTimer(6000, 1000) {


            @Override
            public void onTick(long l) {
                if (getCode == 0) {
                    if (textDistance.getText() != "0" && navigationMapRoute != null) {
                        directionPopup.setVisibility(View.VISIBLE);
                        IconFactory iconFactory = IconFactory.getInstance(MainActivity.this);
                        Icon icon = iconFactory.fromResource(R.drawable.map_marker_dark);
                        loadingPopup.setVisibility(View.GONE);
                        CameraPosition moveCamera = new CameraPosition.Builder().target(new LatLng(11.240103, 124.997382)).zoom(16.7).bearing(170).tilt(30).build(); // ==
                        map.animateCamera(CameraUpdateFactory.newCameraPosition(moveCamera));
                        removeMarkers();
                        map.removeImage("markericon");
                        Bitmap iconmarker = BitmapFactory.decodeResource(getResources(), R.drawable.map_marker_dark);
                        map.addImage("markericon", iconmarker);
                        removeLayers();
                        map.addSource(new GeoJsonSource("marker", FeatureCollection.fromFeature(Feature.fromGeometry(Point.fromLngLat(clickLong, clickLat)))));
                        map.addLayer(new SymbolLayer("markerlayer", "marker").withProperties(PropertyFactory.iconImage("markericon"), iconSize(1f), PropertyFactory.iconOffset(new Float[]{0f, -9f}), PropertyFactory.iconAllowOverlap(false), PropertyFactory.iconIgnorePlacement(false)));
                        //     buildingLocationMarker = map.addMarker(new MarkerOptions().position(new LatLng(clickLat, clickLong)).icon(icon));
                        unclickable();
                        //    longclickmarker.remove();
                        cancel();
                    }
                }
                if (getCode == 1) {
                    if (textDistance.getText() != "0" && navigationMapRoute != null) {

                        directionPopup.setVisibility(View.VISIBLE);
                        IconFactory iconFactory = IconFactory.getInstance(MainActivity.this);
                        Icon icon = iconFactory.fromResource(R.drawable.map_marker_dark);
                        loadingPopup.setVisibility(View.GONE);
                        CameraPosition moveCamera = new CameraPosition.Builder().target(new LatLng(11.240103, 124.997382)).zoom(16.7).bearing(170).tilt(30).build(); // ==
                        map.animateCamera(CameraUpdateFactory.newCameraPosition(moveCamera));
                        removeMarkers();
                        Bitmap iconmarker = BitmapFactory.decodeResource(getResources(), R.drawable.map_marker_dark);
                        map.addImage("markericon", iconmarker);
                        removeLayers();
                        map.addSource(new GeoJsonSource("marker", FeatureCollection.fromFeature(Feature.fromGeometry(Point.fromLngLat(thisLong, thisLat)))));
                        map.addLayer(new SymbolLayer("markerlayer", "marker").withProperties(PropertyFactory.iconImage("markericon"), iconSize(1f), PropertyFactory.iconOffset(new Float[]{0f, -9f}), PropertyFactory.iconAllowOverlap(false), PropertyFactory.iconIgnorePlacement(false)));
                        //   buildingLocationMarker = map.addMarker(new MarkerOptions().position(new LatLng(thisLat, thisLong)).icon(icon));
                        unclickable();
                        cancel();
                    }
                }
                if (getCode == 2) {
                    if (textDistance.getText() != "0" && navigationMapRoute != null) {
                        IconFactory iconFactory = IconFactory.getInstance(MainActivity.this);
                        Icon icon = iconFactory.fromResource(R.drawable.map_marker_dark);
                        directionPopup.setVisibility(View.VISIBLE);
                        loadingPopup.setVisibility(View.GONE);
                        removeMarkers();
                        Bitmap iconmarker = BitmapFactory.decodeResource(getResources(), R.drawable.map_marker_dark);
                        map.addImage("markericon", iconmarker);
                        removeLayers();
                        map.addSource(new GeoJsonSource("marker", FeatureCollection.fromFeature(Feature.fromGeometry(Point.fromLngLat(dLong, dLat)))));
                        map.addLayer(new SymbolLayer("markerlayer", "marker").withProperties(PropertyFactory.iconImage("markericon"), iconSize(1f), PropertyFactory.iconOffset(new Float[]{0f, -9f}), PropertyFactory.iconAllowOverlap(false), PropertyFactory.iconIgnorePlacement(false)));
                        //      buildingLocationMarker = map.addMarker(new MarkerOptions().position(new LatLng(dLat, dLong)).icon(icon));
                        CameraPosition moveCamera = new CameraPosition.Builder().target(new LatLng(11.240103, 124.997382)).zoom(16.7).bearing(180).tilt(30).build();
                        map.moveCamera(CameraUpdateFactory.newCameraPosition(moveCamera));
                        cancel();

                    }
                }
            }

            @Override
            public void onFinish() {

                if (textDistance.getText() == "0") {
                    buildAlertMessageNoInternet();
                    loadingPopup.setVisibility(View.GONE);
                    directionPopup.setVisibility(View.GONE);
                    if (navigationMapRoute != null) {
                        navigationMapRoute.removeRoute();
                    }
                    if (buildingLocationMarker != null) {
                        map.removeMarker(buildingLocationMarker);
                    }
                    locate.setVisibility(View.VISIBLE);
                    cancel();
                }
            }
        };
        cTimer.start();

    }

    public void cancel() {
        if (cTimer != null) {
            cTimer.cancel();
        }
    }

    public void removeLayers() {
        map.removeLayer("cmarkerlayer");
        map.removeSource("cmarker");
        map.removeLayer("markerlayer");
        map.removeSource("marker");

    }

    public void selectMarker(SymbolLayer highlightLayer) {
        ValueAnimator clickAnimator = new ValueAnimator();
        clickAnimator.setObjectValues(1f, 2f);
        clickAnimator.setDuration(300);
        clickAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                highlightLayer.setProperties(PropertyFactory.iconSize((float) valueAnimator.getAnimatedValue()), PropertyFactory.textSize((float) valueAnimator.getAnimatedValue())
                );
            }
        });
        clickAnimator.start();
        selected = 1;

    }

    private void deselectMarker(final SymbolLayer highlightLayer) {
        ValueAnimator clickAnimator = new ValueAnimator();
        clickAnimator.setObjectValues(2f, 1f);
        clickAnimator.setDuration(300);
        clickAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                highlightLayer.setProperties(PropertyFactory.iconSize((float) valueAnimator.getAnimatedValue()), PropertyFactory.textSize((float) valueAnimator.getAnimatedValue()));
            }
        });
        clickAnimator.start();
        selected = 0;

    }


    // ALERT DIALOG


    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("We recommend to turn on your GPS or set your location mode to high accuracy.")
                .setCancelable(true)

                .setPositiveButton("Go to settings", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        }
                )
                .setNegativeButton("Later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void buildAlertMessageNoInternet() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Unable to connect");
        builder.setMessage("Took too long to respond. Please try again")
                .setCancelable(true)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        removeLayers();
                        cancel();
                    }
                })
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (getCode == 0) {
                                    lcGetDirection();
                                }
                                if (getCode == 1) {
                                    searchGetDirection();
                                }
                                if (getCode == 2) {
                                    directionResult();
                                }
                                //   cancel();
                            }
                        }


                );

        final AlertDialog alert = builder.create();

        alert.show();
    }

    private void buildAlertMessageNotConnected() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("You are offline");

        builder.setMessage("Wifi/Mobile data is off. Turn it on to continue.")
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }
                );
        final AlertDialog alert = builder.create();
        alert.show();
    }


    private void buildAlertMessageNoOfflineDatabase() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher_emap_round);

        builder.setMessage("No offline database found. Please connect to the internet and sync.")
                .setCancelable(true)

                .setPositiveButton("Sync now", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                checkDatabaseUpdate();
                            }
                        }
                )
                .setNegativeButton("Later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        final AlertDialog alert = builder.create();
        alert.show();
    }


    public void buildAlertDatabaseUpdateAvailable() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New update");
        builder.setIcon(R.mipmap.ic_launcher_emap_round);
        builder.setMessage("We recommend to sync your data immediately.")
                .setCancelable(false)
                .setPositiveButton("Sync", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                checkDatabaseUpdate();
                            }
                        }
                );
        final AlertDialog alert = builder.create();
        alert.show();
    }


    // LOCATION


    @SuppressWarnings({"MissingPermission"})
    private void enableLocationPlugin() {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            initializeLocationEngine();
            setLocationLayerPlugin();

        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @SuppressWarnings({"MissingPermission"})
    void initializeLocationEngine() {
        LocationEngineProvider locationEngineProvider = new LocationEngineProvider(this);
        locationEngine = locationEngineProvider.obtainBestLocationEngineAvailable();
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.setInterval(MINUTE);
        locationEngine.setFastestInterval(15 * MILLISECONDS_PER_SECOND);
        locationEngine.activate();

        lastLocation = locationEngine.getLastLocation();

        if (lastLocation != null) {
            originLocation = lastLocation;
            locationlatitude = lastLocation.getLatitude();
            locationlongitude = lastLocation.getLongitude();
            //   if(mapView.isShown()) {
            //        setCameraPosition(lastLocation);
            //    }
        } else {
            locationEngine.addLocationEngineListener(this);
        }
    }


    private void setLocationLayerPlugin() {
        locationPlugin = new LocationLayerPlugin(mapView, map);
        locationPlugin.setRenderMode(RenderMode.COMPASS);

        locationPlugin.addOnLocationClickListener(new OnLocationLayerClickListener() {
            @Override
            public void onLocationLayerClick() {
                Toast.makeText(MainActivity.this, "You are here", Toast.LENGTH_SHORT).show();
            }
        });
        getLifecycle().addObserver(locationPlugin);
    }


    private void setCameraPosition(Location location) {
        locationmarker = map.addMarker(new MarkerOptions().position(new LatLng
                (locationlatitude, locationlongitude)));
        if (EVSU_BOUNDS.contains(locationmarker.getPosition())) {
            locationmarker.remove();
            Toast.makeText(MainActivity.this, "Welcome to Eastern Visayas State University",
                    Toast.LENGTH_SHORT).show();
            CameraPosition cameraLocationPosition = new CameraPosition.Builder().target(new LatLng(location.getLatitude(), location.getLongitude())).zoom(20).tilt(50).build();
            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraLocationPosition));
        } else {
            locationmarker.remove();
            Toast.makeText(MainActivity.this, "Too far from EVSU",
                    Toast.LENGTH_SHORT).show();
        }
        //        map.animateCamera(CameraUpdateFactory.newLatLngZoom(
        //                new LatLng(location.getLatitude(), location.getLongitude()), 20 ));

        //     CameraPosition cameraLocationPosition = new CameraPosition.Builder().target(new LatLng(location.getLatitude(), location.getLongitude())).zoom(20).tilt(50).build();
        //     map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraLocationPosition));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocationPlugin();
        } else {
            finish();
        }
    }

    @SuppressWarnings({"MissingPermission"})
    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();

        if (locationEngine != null) {
            //     locationEngine.requestLocationUpdates();
        }
        if (locationPlugin != null) {
            locationPlugin.onStart();

        }
    }

    @SuppressWarnings({"MissingPermission"})
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();

        //Register receiver
        //    LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,new IntentFilter("Success"));

        if (locationPlugin != null) {
            locationPlugin.onStart();
        }
        if (locationEngine != null) {
            //    locationEngine.requestLocationUpdates();

        }
    }

    //  private BroadcastReceiverService mMessageReceiver = new BroadcastReceiverService(){
    //    @Override
    //     public void onReceive(Context context, Intent intent){
    //         String message = intent.getStringExtra("message");
    //          if (message == "data"){
    //             buildAlertDatabaseUpdateAvailable();
    //          }
    //      }
    //   };

    @SuppressWarnings({"MissingPermission"})
    @Override
    protected void onPause() {
        //      LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onPause();
        mapView.onPause();


        if (locationPlugin != null) {
            locationPlugin.onStart();
        }
        if (locationEngine != null) {
            // locationEngine.requestLocationUpdates();

        }
    }

    @SuppressWarnings({"MissingPermission"})
    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
        if (locationPlugin != null) {
            locationPlugin.onStart();
        }
        if (locationEngine != null) {
            //    locationEngine.requestLocationUpdates();

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (locationEngine != null) {
            locationEngine.deactivate();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @SuppressWarnings({"MissingPermission"})
    @Override
    public void onConnected() {
        if (locationEngine != null) {
            //     locationEngine.requestLocationUpdates();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            originLocation = location;
            lastLocation = location;
            setCameraPosition(location);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.targetButton:
                final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();
                } else {
                    initializeLocationEngine();
                    if (originLocation == null) {

                        Toast.makeText(MainActivity.this, "Searching for location", Toast.LENGTH_SHORT).show();
                    } else {
                        targetButton.setVisibility(View.GONE);
                        northButton.setVisibility(View.VISIBLE);
                        locationPlugin.setCameraMode(CameraMode.TRACKING_GPS_NORTH);
                        setCameraPosition(lastLocation);
                    }
                }
                break;
            case R.id.northButton:
                northButton.setVisibility(View.GONE);
                gpsButton.setVisibility(View.VISIBLE);
                setCameraPosition(lastLocation);
                locationPlugin.setCameraMode(CameraMode.TRACKING_COMPASS);
                break;
            case R.id.gpsButton:
                if (originLocation != null) {
                    gpsButton.setVisibility(View.GONE);
                    northButton.setVisibility(View.VISIBLE);
                    setCameraPosition(lastLocation);
                    locationPlugin.setCameraMode(CameraMode.TRACKING_GPS_NORTH);


                } else {
                    Toast.makeText(MainActivity.this, "No current location", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.d:
                //  homeClear();
                if (cd.isConnected()) {
                    searchGetDirection();
                } else {
                    buildAlertMessageNotConnected();
                }
                break;
            case R.id.i:
                if (Search.popup == 1) {
                    Intent dIntent = new Intent(MainActivity.this, Building.class);
                    startActivityForResult(dIntent, 5);
                    roomCode = 1;
                }
                if (Search.popup == 2) {
                    Intent vIntent = new Intent(MainActivity.this, Room.class);
                    startActivityForResult(vIntent, 6);
                    roomCode = 2;

                }
                break;
            case R.id.visibility:
                Layer layer = map.getLayer("layer");
                if (layer != null) {
                    if (VISIBLE.equals(layer.getVisibility().getValue())) {
                        toggleVisibility.setImageDrawable(getResources().getDrawable(R.drawable.ic_text_invisible));
                        layer.setProperties(visibility(NONE));
                    } else {
                        toggleVisibility.setImageDrawable(getResources().getDrawable(R.drawable.ic_text_visible));
                        layer.setProperties(visibility(VISIBLE));
                    }

                }
                break;
            case R.id.lcClick:
                lcGetDirection();
                break;
            case R.id.zoomIn:
                getCurrentZoomLevel();
                Double zoomin = currentZoom + 0.5;
                CameraPosition zoomInCamera = new CameraPosition.Builder().zoom(zoomin).build();
                map.animateCamera(CameraUpdateFactory.newCameraPosition(zoomInCamera));
                break;
            case R.id.zoomOut:
                getCurrentZoomLevel();
                Double zoomout = currentZoom - 0.5;
                CameraPosition zoomOutCamera = new CameraPosition.Builder().zoom(zoomout).build();
                map.animateCamera(CameraUpdateFactory.newCameraPosition(zoomOutCamera));
                break;
            case R.id.zoomOutMap:
                CameraPosition zoomOutMapCamera = new CameraPosition.Builder().zoom(16.5).build();
                map.animateCamera(CameraUpdateFactory.newCameraPosition(zoomOutMapCamera));
                break;
            case R.id.copyText:
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("label", latlngText.getText());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    public void onFling() {
        locationButton();
    }

    @Override
    public void onMapLongClick(@NonNull LatLng point) {
        locate.setVisibility(View.GONE);
        homeClear();
        IconFactory iconFactory = IconFactory.getInstance(MainActivity.this);
        Icon icon = iconFactory.fromResource(R.drawable.map_marker_light);


        Bitmap iconmarker = BitmapFactory.decodeResource(getResources(), R.drawable.map_marker_light);
        map.addImage("markericon1", iconmarker);

        map.removeLayer("markerlayer");
        map.removeSource("marker");


        mapLongClickPoint = Point.fromLngLat(point.getLongitude(), point.getLatitude());
        clickLat = point.getLatitude();
        clickLong = point.getLongitude();

        if (map.getLayer("cmarker") == null) {
            map.removeLayer("cmarkerlayer");
            map.removeSource("cmarker");
            //   longclickmarker = map.addMarker(new MarkerOptions().position(new LatLng
            //         (point.getLatitude(), point.getLongitude())).icon(icon));
            map.addSource(new GeoJsonSource("cmarker", FeatureCollection.fromFeature(Feature.fromGeometry(Point.fromLngLat(clickLong, clickLat)))));
            map.addLayer(new SymbolLayer("cmarkerlayer", "cmarker").withProperties(PropertyFactory.iconImage("markericon1"), iconSize(1f), PropertyFactory.iconAllowOverlap(false)));
        } else {
            //longclickmarker.remove();

            // longclickmarker = map.addMarker(new MarkerOptions().position(new LatLng
            //      (point.getLatitude(), point.getLongitude())).icon(icon));
            map.addSource(new GeoJsonSource("cmarker", FeatureCollection.fromFeature(Feature.fromGeometry(Point.fromLngLat(clickLong, clickLat)))));
            map.addLayer(new SymbolLayer("cmarkerlayer", "cmarker").withProperties(PropertyFactory.iconImage("markericon1"), iconSize(1f), PropertyFactory.iconAllowOverlap(false)));
        }
        Double lcTilt = map.getCameraPosition().tilt;
        Double lcBearing = map.getCameraPosition().bearing;
        CameraPosition moveCamera = new CameraPosition.Builder().target(new LatLng(point.getLatitude(), point.getLongitude())).zoom(18).tilt(lcTilt).bearing(lcBearing).build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(moveCamera), 1000);
        longclickPopup.setVisibility(View.VISIBLE);
        latlngText.setText(new DecimalFormat("##.######").format(clickLat) + "," + new DecimalFormat("##.######").format(clickLong));
        //   clickLat.setText(la.toString());
        //   clickLong.setText(lo.toString());
    }

    @Override
    public void onMapClick(@NonNull LatLng point) {

        removeLayers();
        homeClear();

        PointF fPoint = map.getProjection().toScreenLocation(point);
        List<Feature> features = map.queryRenderedFeatures(fPoint, "layer");
        clickPopup.setVisibility(View.GONE);
        if (!features.isEmpty()) {
            Feature selectedFeature = features.get(0);
            String textId = selectedFeature.id();
            CameraPosition moveCamera = new CameraPosition.Builder().target(new LatLng(point.getLatitude(), point.getLongitude())).zoom(18.5).build();
            map.animateCamera(CameraUpdateFactory.newCameraPosition(moveCamera), 1000);
            homeClear();
            clickPopup.setVisibility(View.VISIBLE);
            clickBuilding.setText(textId);
            clickimageName = selectedFeature.id();
            String clickimage = controller.getClickImage();
            if (clickimage != null) {
                Picasso.get().load("http://design6500.com/assets/public/img/building/" + clickimage).placeholder(R.drawable.no_preview).error(R.drawable.no_preview).into(clickImage, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {


                    }
                });
                // buildingView.setImageBitmap(image);
            } else {
                clickImage.setImageDrawable(getResources().getDrawable(R.drawable.no_preview));
            }



        } else {
            if (clickPopup.getVisibility() == View.VISIBLE) {
              //  AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
             //   animation.setDuration(500);
             //   clickPopup.startAnimation(animation);
                clickPopup.setVisibility(View.GONE);



            }
        }
    }
}
