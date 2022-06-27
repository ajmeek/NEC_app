package com.example.neuroscience;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.fastICA.FastICA;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import static com.example.neuroscience.AudioPatchHelper.transpose;


//TODO: Change from fragment to activity ( currently causing problems to asynchronous app functionality as a fragment)
/**
 * A simple {@link Fragment} subclass.
 * Use the { SimpleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WaveFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int YES = 0, NO = 0;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WaveFragment() {
        // Required empty public constructor
    }

    public static WaveFragment newInstance(){
        return new WaveFragment();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SimpleFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static WaveFragment newInstance(String param1, String param2) {
        WaveFragment fragment = new WaveFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View rootView =  inflater.inflate(R.layout.activity_wave, container, false);

        Bundle bundle = this.getArguments();
        byte[] bm = bundle.getByteArray("bitmap");
        int tmp = 0;

        int sampleSize = bm.length;

        // Get patches from buffer, around 3-5 ms recommended
        double [][] audioPatches = AudioPatchHelper.getPatches(bm, sampleSize, 3, 10000);
        //double [][] audioTransposed = transpose(audioPatches);

        // Run fast ICA on audio patches
        FastICA fastICA = new FastICA();
        try {
            fastICA.fit( audioPatches, 8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        double[][] audioPatchesICA = fastICA.getEM();

        //TODO: Implement a child class of LineGraphSeries or GraphView that initializes with these values instead of
        // individually doing all this
        LineGraphSeries<DataPoint>  series = new LineGraphSeries<>();
        LineGraphSeries<DataPoint>  seriesTwo = new LineGraphSeries<>();
        LineGraphSeries<DataPoint>  seriesThree = new LineGraphSeries<>();
        LineGraphSeries<DataPoint>  seriesFour = new LineGraphSeries<>();

        LineGraphSeries<DataPoint>  seriesFive = new LineGraphSeries<>();
        LineGraphSeries<DataPoint>  seriesSix = new LineGraphSeries<>();
        LineGraphSeries<DataPoint>  seriesSeven = new LineGraphSeries<>();
        LineGraphSeries<DataPoint>  seriesEight = new LineGraphSeries<>();
/*
        LineGraphSeries<DataPoint>  seriesNine = new LineGraphSeries<>();
        LineGraphSeries<DataPoint>  seriesTen = new LineGraphSeries<>();
        LineGraphSeries<DataPoint>  seriesEleven = new LineGraphSeries<>();
        LineGraphSeries<DataPoint>  seriesTwelve = new LineGraphSeries<>();

        LineGraphSeries<DataPoint>  seriesThirteen = new LineGraphSeries<>();
        LineGraphSeries<DataPoint>  seriesFourteen = new LineGraphSeries<>();
        LineGraphSeries<DataPoint>  seriesFifteen = new LineGraphSeries<>();
        LineGraphSeries<DataPoint>  seriesSixteen = new LineGraphSeries<>();
*/
        GraphView graph = (GraphView) rootView.findViewById(R.id.graph_fragment_one);
        GraphView graphTwo = (GraphView) rootView.findViewById(R.id.graph_fragment_two);
        GraphView graphThree = (GraphView) rootView.findViewById(R.id.graph_fragment_three);
        GraphView graphFour = (GraphView) rootView.findViewById(R.id.graph_fragment_four);

        GraphView graphFive = (GraphView) rootView.findViewById(R.id.graph_fragment_five);
        GraphView graphSix = (GraphView) rootView.findViewById(R.id.graph_fragment_six);
        GraphView graphSeven = (GraphView) rootView.findViewById(R.id.graph_fragment_seven);
        GraphView graphEight = (GraphView) rootView.findViewById(R.id.graph_fragment_eight);
/*
        GraphView graphNine = (GraphView) rootView.findViewById(R.id.graph_fragment_nine);
        GraphView graphTen = (GraphView) rootView.findViewById(R.id.graph_fragment_ten);
        GraphView graphEleven = (GraphView) rootView.findViewById(R.id.graph_fragment_eleven);
        GraphView graphTwelve = (GraphView) rootView.findViewById(R.id.graph_fragment_twelve);

        GraphView graphThirteen = (GraphView) rootView.findViewById(R.id.graph_fragment_thirteen);
        GraphView graphFourteen = (GraphView) rootView.findViewById(R.id.graph_fragment_fourteen);
        GraphView graphFifteen = (GraphView) rootView.findViewById(R.id.graph_fragment_fifteen);
        GraphView graphSixteen = (GraphView) rootView.findViewById(R.id.graph_fragment_sixteen);
*/
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graph.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graph.getViewport().setDrawBorder(true);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMaxX(30);

        graphTwo.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graphTwo.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graphTwo.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graphTwo.getViewport().setDrawBorder(true);
        graphTwo.getViewport().setXAxisBoundsManual(true);
        graphTwo.getViewport().setMaxX(30);

        graphThree.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graphThree.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graphThree.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graphThree.getViewport().setDrawBorder(true);
        graphThree.getViewport().setXAxisBoundsManual(true);
        graphThree.getViewport().setMaxX(30);

        graphFour.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graphFour.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graphFour.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graphFour.getViewport().setDrawBorder(true);
        graphFour.getViewport().setXAxisBoundsManual(true);
        graphFour.getViewport().setMaxX(30);

        graphFive.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graphFive.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graphFive.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graphFive.getViewport().setDrawBorder(true);
        graphFive.getViewport().setXAxisBoundsManual(true);
        graphFive.getViewport().setMaxX(30);

        graphSix.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graphSix.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graphSix.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graphSix.getViewport().setDrawBorder(true);
        graphSix.getViewport().setXAxisBoundsManual(true);
        graphSix.getViewport().setMaxX(30);

        graphSeven.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graphSeven.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graphSeven.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graphSeven.getViewport().setDrawBorder(true);
        graphSeven.getViewport().setXAxisBoundsManual(true);
        graphSeven.getViewport().setMaxX(30);

        graphEight.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graphEight.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graphEight.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graphEight.getViewport().setDrawBorder(true);
        graphEight.getViewport().setXAxisBoundsManual(true);
        graphEight.getViewport().setMaxX(30);
/*
        graphNine.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graphNine.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graphNine.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graphNine.getViewport().setDrawBorder(true);
        graphNine.getViewport().setXAxisBoundsManual(true);
        graphNine.getViewport().setMaxX(30);

        graphTen.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graphTen.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graphTen.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graphTen.getViewport().setDrawBorder(true);
        graphTen.getViewport().setXAxisBoundsManual(true);
        graphTen.getViewport().setMaxX(30);

        graphEleven.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graphEleven.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graphEleven.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graphEleven.getViewport().setDrawBorder(true);
        graphEleven.getViewport().setXAxisBoundsManual(true);
        graphEleven.getViewport().setMaxX(30);

        graphTwelve.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graphTwelve.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graphTwelve.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graphTwelve.getViewport().setDrawBorder(true);
        graphTwelve.getViewport().setXAxisBoundsManual(true);
        graphTwelve.getViewport().setMaxX(30);

        graphThirteen.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graphThirteen.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graphThirteen.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graphThirteen.getViewport().setDrawBorder(true);
        graphThirteen.getViewport().setXAxisBoundsManual(true);
        graphThirteen.getViewport().setMaxX(30);

        graphFourteen.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graphFourteen.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graphFourteen.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graphFourteen.getViewport().setDrawBorder(true);
        graphFourteen.getViewport().setXAxisBoundsManual(true);
        graphFourteen.getViewport().setMaxX(30);

        graphFifteen.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graphFifteen.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graphFifteen.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graphFifteen.getViewport().setDrawBorder(true);
        graphFifteen.getViewport().setXAxisBoundsManual(true);
        graphFifteen.getViewport().setMaxX(30);

        graphSixteen.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graphSixteen.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graphSixteen.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graphSixteen.getViewport().setDrawBorder(true);
        graphSixteen.getViewport().setXAxisBoundsManual(true);
        graphSixteen.getViewport().setMaxX(30);
*/

        // Can be used to display raw audio patches

//   for(int i = 0 ; i < 30; i++) {
//       series.appendData(new DataPoint(i, audioPatches[0][i]), true, 30);
//       seriesTwo.appendData(new DataPoint(i, audioPatches[1][i]), true, 30);
//       seriesThree.appendData(new DataPoint(i, audioPatches[2][i]), true, 30);
//       seriesFour.appendData(new DataPoint(i, audioPatches[3][i]), true, 30);
//       seriesFive.appendData(new DataPoint(i, audioPatches[4][i]), true, 30);
//       seriesSix.appendData(new DataPoint(i, audioPatches[5][i]), true, 30);
//       seriesSeven.appendData(new DataPoint(i, audioPatches[6][i]), true, 30);
//       seriesEight.appendData(new DataPoint(i, audioPatches[7][i]), true, 30);
//       seriesNine.appendData(new DataPoint(i, audioPatches[8][i]), true, 30);
//       seriesTen.appendData(new DataPoint(i, audioPatches[9][i]), true, 30);
//       seriesEleven.appendData(new DataPoint(i, audioPatches[10][i]), true, 30);
//       seriesTwelve.appendData(new DataPoint(i, audioPatches[11][i]), true, 30);
//       seriesThirteen.appendData(new DataPoint(i, audioPatches[12][i]), true, 30);
//       seriesFourteen.appendData(new DataPoint(i, audioPatches[13][i]), true, 30);
//       seriesFifteen.appendData(new DataPoint(i, audioPatches[14][i]), true, 30);
//       seriesSixteen.appendData(new DataPoint(i, audioPatches[15][i]), true, 30);
//   }

        //audioPatchesICA = transpose(audioPatchesICA);

        // Used to display processed audio patches
       for(int i = 0 ; i < 30; i++) {
           series.appendData(new DataPoint(i, audioPatchesICA[0][i]), true, 30);
           seriesTwo.appendData(new DataPoint(i, audioPatchesICA[1][i]), true, 30);
           seriesThree.appendData(new DataPoint(i, audioPatchesICA[2][i]), true, 30);
           seriesFour.appendData(new DataPoint(i, audioPatchesICA[3][i]), true, 30);
           seriesFive.appendData(new DataPoint(i, audioPatchesICA[4][i]), true, 30);
           seriesSix.appendData(new DataPoint(i, audioPatchesICA[5][i]), true, 30);
           seriesSeven.appendData(new DataPoint(i, audioPatchesICA[6][i]), true, 30);
           seriesEight.appendData(new DataPoint(i, audioPatchesICA[7][i]), true, 30);
//           seriesNine.appendData(new DataPoint(i, audioPatchesICA[8][i]), true, 30);
//           seriesTen.appendData(new DataPoint(i, audioPatchesICA[9][i]), true, 30);
//           seriesEleven.appendData(new DataPoint(i, audioPatchesICA[10][i]), true, 30);
//           seriesTwelve.appendData(new DataPoint(i, audioPatchesICA[11][i]), true, 30);
//           seriesThirteen.appendData(new DataPoint(i, audioPatchesICA[12][i]), true, 30);
//           seriesFourteen.appendData(new DataPoint(i, audioPatchesICA[13][i]), true, 30);
//           seriesFifteen.appendData(new DataPoint(i, audioPatchesICA[14][i]), true, 30);
//           seriesSixteen.appendData(new DataPoint(i, audioPatchesICA[15][i]), true, 30);
        }

        series.setThickness(3);
        seriesTwo.setThickness(3);
        seriesThree.setThickness(3);
        seriesFour.setThickness(3);

        seriesFive.setThickness(3);
        seriesSix.setThickness(3);
        seriesSeven.setThickness(3);
        seriesEight.setThickness(3);
/*
        seriesNine.setThickness(3);
        seriesTen.setThickness(3);
        seriesEleven.setThickness(3);
        seriesTwelve.setThickness(3);

        seriesThirteen.setThickness(3);
        seriesFourteen.setThickness(3);
        seriesFifteen.setThickness(3);
        seriesSixteen.setThickness(3);
*/
        graph.addSeries(series);
        graphTwo.addSeries(seriesTwo);
        graphThree.addSeries(seriesThree);
        graphFour.addSeries(seriesFour);

        graphFive.addSeries(seriesFive);
        graphSix.addSeries(seriesSix);
        graphSeven.addSeries(seriesSeven);
        graphEight.addSeries(seriesEight);
/*
        graphNine.addSeries(seriesNine);
        graphTen.addSeries(seriesTen);
        graphEleven.addSeries(seriesEleven);
        graphTwelve.addSeries(seriesTwelve);

        graphThirteen.addSeries(seriesThirteen);
        graphFourteen.addSeries(seriesFourteen);
        graphFifteen.addSeries(seriesFifteen);
        graphSixteen.addSeries(seriesSixteen);
*/
        return rootView;
    }

    //TODO:Remove this function and its call (DONE)
//    @Override
//    public void onClick(View v) {
//        //if (v.getId() == R.id.close_button){
//            //getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
//        //}
//    }
}

