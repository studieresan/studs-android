package se.studieresan.studs.util

object GraphQLHelper {
    fun getFormsQuery(userId: String, eventId: String) =
        """query {
            |eventForms(userId: "$userId", eventId: "$eventId") {
            |id,
            |createdAt,
            |updatedAt,
            |userId,
            |eventId,
        |... on PreEventForm {
            |interestInRegularWorkBefore,
            |interestInCompanyMotivationBefore,
            |familiarWithCompany,
            |viewOfCompany,
        |},
        |... on PostEventForm {
            |interestInRegularWork,
            |interestInCompanyMotivation,
            |eventImprovements,
            |eventFeedback,
            |foodRating,
            |activitiesRating,
            |atmosphereRating,
            |qualifiedToWork,
            |eventImpact}}}""".trimMargin()
}
