package com.nextweb.nwapplogger;

import java.util.HashMap;
import java.util.Map;

public class LogBuilders {
	private static final QueryBuilder queryBuilder = QueryBuilder.getInstance();

	static String build(Map<String, String> bindMap) {
		return queryBuilder.getURLQuery(bindMap);
	}

	public static class EventLogBuilder {
		private static Map<String, String> bindMap;

		public Event setEvent(String type) {
			bindMap = new HashMap<String, String>();
			return new Event(type);
		}

		public Event setEvent(String type, String desc) {
			bindMap = new HashMap<String, String>();
			return new Event(type, desc);
		}

		interface Builder {
			public String build();
		}

		public class Event implements Builder {
			public Event(String type) {
				bindMap.put(Constants.params.evtType, type);
			}

			public Event(String type, String desc) {
				bindMap.put(Constants.params.evtType, type);
				bindMap.put(Constants.params.evtDesc, desc);
			}

			public String build() {
				return queryBuilder.getURLQuery(bindMap);
			}

			public Category setCategory(String id) {
				return new Category(id);
			}

			public class Category implements Builder {
				public Category(String id) {
					bindMap.put(Constants.params.cateId, id);
				}

				public String build() {
					return queryBuilder.getURLQuery(bindMap);
				}
			}
		}
	}
}
