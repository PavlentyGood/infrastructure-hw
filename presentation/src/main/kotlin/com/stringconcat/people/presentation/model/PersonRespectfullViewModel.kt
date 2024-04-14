package com.stringconcat.people.presentation.model

import com.stringconcat.people.businessPeople.Person

class PersonRespectfullViewModel(
        private val person: Person
) {
    companion object {
        const val MISTER_PREFIX_AGE = 40
    }

    fun title() =
            "${prefixIfNeeded()} ${person.firstName} ${person.secondName}"

    private fun prefixIfNeeded() =
            if (person.age() > MISTER_PREFIX_AGE)
                when (person.sex) {
                    Person.Sex.MAN -> "Mr"
                    Person.Sex.WOMAN -> "Mrs"
                }
            else ""


    fun avatarUrl() = person.avatartUrl

    fun birthDate() = "${person.birthDate.dayOfMonth} ${person.birthDate.month} ${person.birthDate.year}"

    fun favoriteQuote() = person.favoriteQuote
}
