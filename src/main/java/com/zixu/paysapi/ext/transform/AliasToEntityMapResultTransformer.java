package com.zixu.paysapi.ext.transform;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.transform.AliasedTupleSubsetResultTransformer;

import com.zixu.paysapi.ext.util.NameUtil;

public class AliasToEntityMapResultTransformer extends AliasedTupleSubsetResultTransformer {
	private static final long serialVersionUID = 5313715124861249867L;
	public static final AliasToEntityMapResultTransformer INSTANCE = new AliasToEntityMapResultTransformer();

	private AliasToEntityMapResultTransformer() {
	}

	@Override
	public Object transformTuple(Object[] tuple, String[] aliases) {
		Map<String, Object> result = new HashMap<String, Object>(tuple.length);
		for (int i = 0; i < tuple.length; i++) {
			String alias = aliases[i];
			if (alias != null) {
				result.put(NameUtil.underscoreNameToPropertyName(alias), tuple[i]);

			}
		}
		return result;
	}

	@Override
	public boolean isTransformedValueATupleElement(String[] aliases, int tupleLength) {
		return false;
	}

	private Object readResolve() {
		return INSTANCE;
	}
}
