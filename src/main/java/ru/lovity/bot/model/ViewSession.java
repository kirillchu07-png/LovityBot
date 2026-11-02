package ru.lovity.bot.model;

import java.util.Arrays;

public class ViewSession {

    private final String[] ids;
    private final int[] scores;
    private final int count;

    private final String[] likes;

    private int likeCount = 0;
    private int index = 0;

    public ViewSession(
            String[] ids,
            int[] scores,
            int count) {

        this.ids = Arrays.copyOf(ids, count);
        this.scores = Arrays.copyOf(scores, count);
        this.count = count;

        this.likes = new String[count];
    }

    public String[] getIds() {
        return ids;
    }

    public int[] getScores() {
        return scores;
    }

    public int getCount() {
        return count;
    }

    public int getIndex() {
        return index;
    }

    public void nextProfile() {
        index++;
    }

    public void addLike(String id) {
        likes[likeCount] = id;
        likeCount++;
    }

    public String[] getLikes() {
        return Arrays.copyOf(likes, likeCount);
    }

    public int getLikeCount() {
        return likeCount;
    }
}