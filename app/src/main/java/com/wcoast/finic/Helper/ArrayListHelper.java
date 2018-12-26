package com.wcoast.finic.Helper;

import com.wcoast.finic.model.ModelNameImageLink;
import com.wcoast.finic.model.ModelNameLink;

import java.util.ArrayList;

public class ArrayListHelper {

    private ArrayList<ModelNameLink> modelNameLinkArrayList = new ArrayList<>();

    public void setDataForPS2() {
        modelNameLinkArrayList.add(new ModelNameLink("ic_bionicle", "https://www.emuparadise.me/Sony_Playstation_2_ISOs/Bionicle_(USA)/150195"));
        modelNameLinkArrayList.add(new ModelNameLink("ic_final_fantasy", "http://finalfantasy.wikia.com/wiki/PlayStation_2"));
        modelNameLinkArrayList.add(new ModelNameLink("ic_jak3", "https://www.giantbomb.com/jak-3/3030-9669/"));
        modelNameLinkArrayList.add(new ModelNameLink("ic_kingdom_hearts", "https://gamefaqs.gamespot.com/ps2/516587-kingdom-hearts/saves"));
        modelNameLinkArrayList.add(new ModelNameLink("ic_racer_revenge", "https://www.carthrottle.com/post/w3brj3l"));
        modelNameLinkArrayList.add(new ModelNameLink("ic_retro", "https://www.chilloutgames.co.uk/PS2/Sony_PlayStation_2_Retro.aspx"));
    }

    public void setDataForXBox() {
        modelNameLinkArrayList.add(new ModelNameLink("ic_cod", "https://www.xbox.com/en-US/games/call-of-duty-black-ops-4"));
        modelNameLinkArrayList.add(new ModelNameLink("ic_gears_of_war", "https://www.xbox.com/en-US/games/gears"));
        modelNameLinkArrayList.add(new ModelNameLink("ic_just_cause", "https://www.microsoft.com/en-in/p/just-cause-3/br2f4dg4wxf8#activetab=pivot:overviewtab"));
        modelNameLinkArrayList.add(new ModelNameLink("ic_lego", "https://www.amazon.com/Jurassic-World-Evolution-Xbox-One/dp/B07C4SGGZ2"));
        modelNameLinkArrayList.add(new ModelNameLink("ic_titanfall", "https://www.microsoft.com/en-in/p/titanfall-2/c0x2hnvh08fb#activetab=pivot:overviewtab"));

    }

    public void setDataForKineet() {
        modelNameLinkArrayList.add(new ModelNameLink("ic_double_dragon", "https://www.playstation.com/en-us/games/arcade-archives-double-dragon-ps4/"));
        modelNameLinkArrayList.add(new ModelNameLink("ic_fantasia", "https://www.playstation.com/en-us/games/ephemeral-fantasia-ps2/"));
        modelNameLinkArrayList.add(new ModelNameLink("ic_harry_potter", "https://www.playstation.com/en-in/games/harry-potter-and-the-philosophers-stone-ps2/"));
        modelNameLinkArrayList.add(new ModelNameLink("ic_sesame", "https://www.sesamestreet.org/games"));
        modelNameLinkArrayList.add(new ModelNameLink("ic_surf_skate", "https://www.metacritic.com/game/xbox-360/spongebobs-surf-skate-roadtrip"));

    }

    public ArrayList<ModelNameImageLink> getBusinessImage() {
        ArrayList<ModelNameImageLink> imageNames = new ArrayList<>();
        imageNames.add(new ModelNameImageLink("Bizztor", "ic_bizztor", "https://bizztor.com/in"));
        imageNames.add(new ModelNameImageLink("ClearTax", "ic_cleartax", "https://cleartax.in"));
        imageNames.add(new ModelNameImageLink("Dheya Career Mentors", "ic_dheya_career_mentors_1", "https://www.dheya.com/"));

        return imageNames;
    }

    public ArrayList<ModelNameImageLink> getEducationImage() {
        ArrayList<ModelNameImageLink> imageNames = new ArrayList<>();
        imageNames.add(new ModelNameImageLink("Academia", "ic_academia_logo", "http://www.academia.edu"));
        imageNames.add(new ModelNameImageLink("Mindler Big Blue", "ic_mindler_big_blue", "https://www.mindler.com/"));
        imageNames.add(new ModelNameImageLink("Dheya Career Mentors", "ic_dheya_career_mentors_1", "https://www.dheya.com/"));

        return imageNames;
    }

    public ArrayList<ModelNameImageLink> getCareerImage() {
        ArrayList<ModelNameImageLink> imageNames = new ArrayList<>();
        imageNames.add(new ModelNameImageLink("Career Futura", "ic_careerfutura_logo", "http://blog.careerfutura.com/"));
        imageNames.add(new ModelNameImageLink("Career Guide", "ic_careerguide_logo", "https://www.careerguide.com/"));
        imageNames.add(new ModelNameImageLink("I Dream Career", "ic_idc_logo", "https://idreamcareer.com/"));

        return imageNames;
    }

    public ArrayList<ModelNameLink> getTicketBookingImageNames() {
        ArrayList<ModelNameLink> imageNames = new ArrayList<>();
        imageNames.add(new ModelNameLink("ic_bus", "https://www.redbus.in/"));
        imageNames.add(new ModelNameLink("ic_gologo", "https://www.goibibo.com/"));
        imageNames.add(new ModelNameLink("ic_irctc", "https://www.irctc.co.in/"));
        imageNames.add(new ModelNameLink("ic_mmtlogo", "https://www.makemytrip.com/"));
        imageNames.add(new ModelNameLink("ic_paytm", "https://paytm.com/train-tickets"));

        return imageNames;
    }

    public ArrayList<ModelNameLink> getOnlineShoppingImageNames() {
        ArrayList<ModelNameLink> imageNames = new ArrayList<>();

        imageNames.add(new ModelNameLink("ic_all_store", "https://www.allonlinestore.in/"));
        imageNames.add(new ModelNameLink("ic_amazon_tb", "https://www.amazon.in"));
        imageNames.add(new ModelNameLink("ic_flipkart", "https://www.flipkart.com/"));
        imageNames.add(new ModelNameLink("ic_indiarush", "https://indiarush.com/"));
        imageNames.add(new ModelNameLink("ic_jabong", "https://www.jabong.com/"));
        imageNames.add(new ModelNameLink("ic_kraftly", "https://kraftly.com/"));
        imageNames.add(new ModelNameLink("ic_mirraw", "https://www.mirraw.com/"));
        imageNames.add(new ModelNameLink("ic_shopclues", "https://www.snapdeal.com/"));
        imageNames.add(new ModelNameLink("ic_snapdeal", "https://www.shopclues.com/"));
        imageNames.add(new ModelNameLink("ic_voyalla", "https://www.voylla.com/"));

        return imageNames;
    }

    public ArrayList<ModelNameLink> getBillPaymentImageNames() {
        ArrayList<ModelNameLink> imageNames = new ArrayList<>();
        imageNames.add(new ModelNameLink("ic_connect_broadband", "https://www.connectzone.in/"));
        imageNames.add(new ModelNameLink("ic_freecharge", "https://www.freecharge.in"));
        imageNames.add(new ModelNameLink("ic_mobikwik", "https://www.mobikwik.com/"));
        imageNames.add(new ModelNameLink("ic_paytm", "https://paytm.com/"));

        return imageNames;
    }

    public ArrayList<ModelNameLink> getOnlineGameImageNames() {
        ArrayList<ModelNameLink> imageNames = new ArrayList<>();
        imageNames.add(new ModelNameLink("ic_biglogo4", "https://www.onlinegames.com/"));
        imageNames.add(new ModelNameLink("ic_arkadium", "https://www.arkadium.com/"));
        imageNames.add(new ModelNameLink("ic_playok5", "https://www.playok.com/en/ludo/"));
        imageNames.add(new ModelNameLink("ic_rummymillionaire", "https://www.rummymillionaire.com/?"));
        imageNames.add(new ModelNameLink("ic_rummy_circle", "https://www.rummycircle.com/?"));

        return imageNames;
    }

    public ArrayList<ModelNameLink> getModelNameLinkArrayList() {
        return modelNameLinkArrayList;
    }
}
