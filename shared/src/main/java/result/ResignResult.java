package result;

import model.GameData;

public record ResignResult (GameData game, String winner, String loser){
}
