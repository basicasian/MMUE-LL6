package at.ac.tuwien.mmue_ll6.persistence;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "score")
public class Score {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public float time;

    public Score(float time) {
        this.time = time;
    }
}