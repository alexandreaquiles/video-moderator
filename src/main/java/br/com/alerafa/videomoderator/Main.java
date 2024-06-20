package br.com.alerafa.videomoderator;

public class Main {

    public static void main(String[] args) throws Exception {
        var videoModerator = new VideoModerator();
        String output = videoModerator.moderate("gs://vm-videos/escalada.mp4");
        System.out.println(output);
    }

}
