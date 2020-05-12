package marvel;

import com.opencsv.bean.CsvBindByName;

/*
 *  This is the Java representation of a Marvel Hero
 */
public class MarvelHeroModel {
    /*
     *  name of the marvel hero
     */
    @CsvBindByName
    private String hero;

    /*
     *  comic's title where the marvel hero appeared
     */
    @CsvBindByName
    private String book;

    /*
     *  Where abstraction function would go if an ADT, but it's not
     */

    /*
     *  Where rep invariant would go if an ADT, but it's not
     */

    /*
     *  returns the name of the hero
     */
    public String getName() {
        return hero;
    }

    /*
     *  sets the name of the hero
     */
    public void setName(String name) {
        this.hero = name;
    }

    /*
     *  returns the title of the comic the hero appeared in
     */
    public String getTitle() {
        return book;
    }

    /*
     *  sets the title of the comic the hero appeared in
     */
    public void setTitle(String title) {
        book = title;
    }
}
