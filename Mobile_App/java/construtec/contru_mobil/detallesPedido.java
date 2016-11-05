package com.example.emmanuel.construmobil;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class detallesPedido extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_pedido);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position){
                case 1: return pedidoDetails.newInstance(position + 1);
                default: return productList.newInstance(position + 1);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Información del Usuario";
                case 1:
                    return "Mis Pedidos";
                case 2:
                    return "Productos";
                case 3:
                    return "Categorías";
                case 4:
                    return "Opciones";
            }
            return null;
        }
    }

    /**
     * A view containing a user's details
     */
    public static class pedidoDetails extends Fragment {

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public pedidoDetails() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static pedidoDetails newInstance(int sectionNumber) {
            pedidoDetails fragment = new pedidoDetails();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        /**
         * If a view of the project list is created this is called
         * @param inflater required by Android
         * @param container required by Android
         * @param savedInstanceState required by Android
         * @return View to be displayed
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.pedido_information, container, false);

            //TODO add updates

            return rootView;
        }
    }

    /**
     * A view containing a list of all one users requests
     */
    public static class productList extends Fragment {

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public productList() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static productList newInstance(int sectionNumber) {
            productList fragment = new productList();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        /**
         * If a view of the project list is created this is called
         * @param inflater required by Android
         * @param container required by Android
         * @param savedInstanceState required by Android
         * @return View to be displayed
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.pager_view_list, container, false);

            //TODO populate list from my productList

            return rootView;
        }
    }
}
