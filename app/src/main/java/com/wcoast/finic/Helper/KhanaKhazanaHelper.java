package com.wcoast.finic.Helper;

import com.wcoast.finic.model.ModelKhanaKhazana;

import java.util.ArrayList;
import java.util.Arrays;

public class KhanaKhazanaHelper {


    public static ArrayList<ModelKhanaKhazana> getKhanaKhazanaList() {
        ArrayList<ModelKhanaKhazana> khanaKhazanaList = new ArrayList<>();
        khanaKhazanaList.add(new ModelKhanaKhazana(101, "Chinese", new ArrayList<>(Arrays.asList("img_chinese_1", "img_chinese_2", "img_chinese_3", "img_chinese_4", "img_chinese_5")),"ic_chinese"));
        khanaKhazanaList.add(new ModelKhanaKhazana(102, "Home-Tips", new ArrayList<>(Arrays.asList("img_hometip_1", "img_hometip_2", "img_hometip_3", "img_hometip_4")),"ic_home_tips"));
        khanaKhazanaList.add(new ModelKhanaKhazana(103, "Kitchen Dictionary", new ArrayList<>(Arrays.asList("img_kitchen_dictionary_1", "img_kitchen_dictionary_2", "img_kitchen_dictionary_3", "img_kitchen_dictionary_4")),"ic_home_dictionary"));
        khanaKhazanaList.add(new ModelKhanaKhazana(104, "Sweets", new ArrayList<>(Arrays.asList("img_sweets_1", "img_sweets_2", "img_sweets_3", "img_sweets_4")),"ic_sweets"));
        khanaKhazanaList.add(new ModelKhanaKhazana(105, "Masala", new ArrayList<>(Arrays.asList("img_masala_1", "img_masala_2", "img_masala_3", "img_masala_4", "img_masala_5", "img_masala_6")),"ic_masala"));
        khanaKhazanaList.add(new ModelKhanaKhazana(106, "Naan", new ArrayList<>(Arrays.asList("img_naan_1", "img_naan_2", "img_naan_3", "img_naan_4")),"ic_naan"));
        khanaKhazanaList.add(new ModelKhanaKhazana(107, "Baking", new ArrayList<>(Arrays.asList("img_baking_1", "img_baking_2", "img_baking_3", "img_baking_4")),"ic_baking"));
        khanaKhazanaList.add(new ModelKhanaKhazana(108, "Ice-Cream Shakes", new ArrayList<>(Arrays.asList("img_icecream_1", "img_icecream_2", "img_icecream_3", "img_icecream_4", "img_icecream_5")),"ic_ice_cream_shakes"));
        khanaKhazanaList.add(new ModelKhanaKhazana(109, "Soup", new ArrayList<>(Arrays.asList("img_soup_1", "img_soup_2", "img_soup_3", "img_soup_4")),"ic_soup"));
        khanaKhazanaList.add(new ModelKhanaKhazana(110, "Snacks", new ArrayList<>(Arrays.asList("img_snacks_1", "img_snacks_2", "img_snacks_3", "img_snacks_4")),"ic_snacks"));
        khanaKhazanaList.add(new ModelKhanaKhazana(111, "Aachar Murabba", new ArrayList<>(Arrays.asList("img_achar_1", "img_achar_2", "img_achar_3", "img_achar_4", "img_achar_5")),"ic_achar"));
       // khanaKhazanaList.add(new ModelKhanaKhazana(112, "Sharbat", new ArrayList<>(Arrays.asList("img_chinese_1", "img_chinese_2", "img_chinese_3", "img_chinese_4", "img_chinese_5")),"ic_sharbat"));
        khanaKhazanaList.add(new ModelKhanaKhazana(113, "Pulav", new ArrayList<>(Arrays.asList("img_pulav_1", "img_pulav_2", "img_pulav_3", "img_pulav_4")),"ic_pulav"));

        return khanaKhazanaList;
    }

}
