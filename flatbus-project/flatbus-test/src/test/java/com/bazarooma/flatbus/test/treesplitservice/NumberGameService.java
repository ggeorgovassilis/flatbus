package com.bazarooma.flatbus.test.treesplitservice;

import com.bazarooma.flatbus.shared.BusListener;

/**
 * A simple number game:
 * 1. get a positive integer N
 * 2. if N is 1 return 1
 * 3. if N is 2 or 0 return 0
 * 4. else, divide it into two equal parts A B. For odd N round A up and B down.
 * 5. return game(A) + game(B)
 * @author ggeorgovassilis
 *
 */
public interface NumberGameService extends BusListener{

	int play(int n);
}
