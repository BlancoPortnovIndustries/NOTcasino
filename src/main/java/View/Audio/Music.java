package View.Audio;

public class Music {

    public static void init(){
        JukeBox.load("/Music/MP3/Background.mp3", "Menu");
        JukeBox.setVolume("Menu", 0.6f);
        JukeBox.load("/SFX/MP3/flipping.mp3", "flip");
        JukeBox.setVolume("flip", 0.6f);
        JukeBox.load("/Music/MP3/SelectGame.mp3", "SelectGame");
        JukeBox.setVolume("SelectGame", 0.6f);
        JukeBox.load("/SFX/MP3/StartGame.mp3", "StartGame");
        JukeBox.setVolume("StartGame", 0.6f);
        JukeBox.load("/SFX/MP3/pickCards.mp3", "pickCards");
        JukeBox.setVolume("pickCards", 0.6f);
        JukeBox.load("/Music/MP3/Intro.mp3", "Intro");
        JukeBox.setVolume("Intro", 0.6f);
        JukeBox.load("/SFX/MP3/Lose.mp3", "Lose");
        JukeBox.setVolume("Lose", 0.8f);
        JukeBox.load("/Music/MP3/waiting.mp3", "waiting");
        JukeBox.setVolume("waiting", 0.6f);
        JukeBox.load("/SFX/MP3/pressBtn.mp3", "pressBtn");
        JukeBox.setVolume("pressBtn", 0.8f);
        JukeBox.load("/SFX/MP3/InsertCoin.mp3", "InsertCoin");
        JukeBox.setVolume("InsertCoin", 0.8f);
        JukeBox.load("/Music/MP3/BlackJack.mp3", "BlackJack");
        JukeBox.setVolume("BlackJack", 0.6f);
    }
}
