package com.Test;

import lombok.Data;

@Data
public class SonC {

    private inner inner;

    static class inner{
        String name = "1";

        public inner(String a) {
            this.name = a;
        }
    }

    public static void main(String[] args) throws Exception {

    }

}
