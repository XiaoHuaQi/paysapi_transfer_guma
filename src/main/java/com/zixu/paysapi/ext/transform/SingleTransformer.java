package com.zixu.paysapi.ext.transform;

import org.hibernate.transform.AliasedTupleSubsetResultTransformer;

public class SingleTransformer extends AliasedTupleSubsetResultTransformer {
	private static final long serialVersionUID = 5313715124861249867L;
	public static final SingleTransformer INSTANCE = new SingleTransformer();

	private SingleTransformer() {
	}

	@Override
	public Object transformTuple(Object[] tuple, String[] aliases) {
		String alias = aliases[0];
		if (alias != null) {
			return tuple[0];
		}
		return null;
	}

	@Override
	public boolean isTransformedValueATupleElement(String[] aliases, int tupleLength) {
		return false;
	}

	private Object readResolve() {
		return INSTANCE;
	}
}
