package com.anish.screen;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.Random;

import com.anish.calabashbros.BubbleSorter;
import com.anish.calabashbros.Calabash;
import com.anish.calabashbros.MinionSorter;
import com.anish.calabashbros.Minions;
import com.anish.calabashbros.World;

import asciiPanel.AsciiPanel;

public class WorldScreen implements Screen {

    private World world;
    private Calabash[] bros;
    private Minions[][] minions;
    String[] sortSteps;

    public WorldScreen() {
        world = new World();

        bros = new Calabash[7];
        minions = new Minions[4][4];

        // bros[3] = new Calabash(new Color(204, 0, 0), 1, world);
        // bros[5] = new Calabash(new Color(255, 165, 0), 2, world);
        // bros[1] = new Calabash(new Color(252, 233, 79), 3, world);
        // bros[0] = new Calabash(new Color(78, 154, 6), 4, world);
        // bros[4] = new Calabash(new Color(50, 175, 255), 5, world);
        // bros[6] = new Calabash(new Color(114, 159, 207), 6, world);
        // bros[2] = new Calabash(new Color(173, 127, 168), 7, world);

        // world.put(bros[0], 10, 10);
        // world.put(bros[1], 12, 10);
        // world.put(bros[2], 14, 10);
        // world.put(bros[3], 16, 10);
        // world.put(bros[4], 18, 10);
        // world.put(bros[5], 20, 10);
        // world.put(bros[6], 22, 10);
        int l=16;
        int[] order = new int[l];
        Random R = new Random();
        for(int i=0;i<l;i++){
            int randomNum = R.nextInt(l);
            while(existed(randomNum, order, i)) {
                randomNum = R.nextInt(l);
            }
            order[i]=randomNum;
        }
        int t = 0;

        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                minions[i][j]=new Minions(new Color(order[t]*15,255-order[t]*15,0), order[t], world);
                world.put(minions[i][j],10 + j*2,10 + i*2);
                t++;
            }
        }
        

        MinionSorter<Minions> m = new MinionSorter<>();
        m.load(minions);
        m.sort();

        // BubbleSorter<Calabash> b = new BubbleSorter<>();
        // b.load(bros);
        // b.sort();

        sortSteps = this.parsePlan(m.getPlan());
    }

    private String[] parsePlan(String plan) {
        return plan.split("\n");
    }

    private void execute(Calabash[] bros, String step) {
        String[] couple = step.split("<->");
        getMinByRank(minions, Integer.parseInt(couple[0])).swap(getMinByRank(minions, Integer.parseInt(couple[1])));
    }

    private Calabash getBroByRank(Calabash[] bros, int rank) {
        for (Calabash bro : bros) {
            if (bro.getRank() == rank) {
                return bro;
            }
        }
        return null;
    }

    private Minions getMinByRank(Minions[][] bros, int rank) {
        for (int i=0;i<bros.length;i++) {
            for(int j=0;j<bros[i].length;j++)
            if (bros[i][j].getRank() == rank) {
                return bros[i][j];
            }
        }
        return null;
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {

        for (int x = 0; x < World.WIDTH; x++) {
            for (int y = 0; y < World.HEIGHT; y++) {

                terminal.write(world.get(x, y).getGlyph(), x, y, world.get(x, y).getColor());

            }
        }
    }

    int i = 0;

    @Override
    public Screen respondToUserInput(KeyEvent key) {

        if (i < this.sortSteps.length) {
            this.execute(bros, sortSteps[i]);
            i++;
        }

        return this;
    }
    public static boolean existed(int num, int[] array, int index) {
		for(int i=0; i<index; i++) {
			if(num == array[i]) {
				return true;
			}
		}
		return false;
	}

}
