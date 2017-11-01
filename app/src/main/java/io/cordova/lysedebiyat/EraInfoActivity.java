package io.cordova.lysedebiyat;

import java.io.Serializable;

public class EraInfoActivity implements Serializable {
    private String info;
    private String authors;
    private String link;

    public EraInfoActivity(String info, String authors, String link) {
        this.info = info;
        this.link = link;

        this.authors = createBulletList(authors);
    }

    private String createBulletList(String authors) {

        String[] seperateAuthors = authors.split("\n");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < seperateAuthors.length; i++) {
            sb.append("• ");
            sb.append(seperateAuthors[i]);
            if (i != seperateAuthors.length - 1) {
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }
}
