package com.lollipop.auditory.data

/**
 * 排序顺序
 */
object SortOrder {

    /**
     * 歌曲排序顺序
     */
    enum class Songs {
        BY_TITLE_ASC,
        BY_TITLE_DESC,
        BY_DATE_ADDED_ASC,
        BY_DATE_ADDED_DESC,
        BY_ARTIST_ASC,
        BY_ARTIST_DESC,
        BY_YEAR_ASC,
        BY_YEAR_DESC,
        BY_RANDOM,
    }

    /**
     * 专辑排序顺序
     */
    enum class Albums {
        BY_TITLE_ASC,
        BY_TITLE_DESC,
        BY_DATE_ADDED_ASC,
        BY_DATE_ADDED_DESC,
        BY_ALBUMS_ARTIST_ASC,
        BY_ALBUMS_ARTIST_DESC,
        BY_YEAR_ASC,
        BY_YEAR_DESC,
        BY_RANDOM,
    }

    /**
     * 艺术家排序顺序
     */
    enum class Artists {
        BY_TITLE_ASC,
        BY_TITLE_DESC,
        BY_RANDOM,
    }

}