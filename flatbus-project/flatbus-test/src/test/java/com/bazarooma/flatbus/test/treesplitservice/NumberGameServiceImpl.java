package com.bazarooma.flatbus.test.treesplitservice;

import com.bazarooma.flatbus.test.OurEventBus;

public class NumberGameServiceImpl implements NumberGameService{

	private OurEventBus eventBus;
	
	public NumberGameServiceImpl(OurEventBus eventBus){
		this.eventBus = eventBus;
	}
	
	@Override
	public int play(int n) {
		switch(n){
			case 0:
			case 2:
				eventBus.cancelCurrentEvent();
				return 0;
			case 1:
				eventBus.cancelCurrentEvent();
				return 1;
			default:
				int a = (n/2);
				int b = (n/2) + n%2;
				return eventBus.play(a) + eventBus.play(b);
		}
	}

}
