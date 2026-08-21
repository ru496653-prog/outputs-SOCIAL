package com.example.outputs.data.local

import com.example.outputs.data.model.AnonymousGroup
import com.example.outputs.data.model.EvidenceType
import com.example.outputs.data.model.GenreTheme
import com.example.outputs.data.model.MysteryStatus
import com.example.outputs.data.model.PostType
import com.example.outputs.data.model.PrivacyMode

object InitialData {

    suspend fun populateDatabase(dao: OutputsDao) {
        val userSession = UserSessionEntity(
            userId = "user_anon_849201",
            isAgeVerified = true,
            dob = "1996-04-12",
            citizenshipCountry = "Verified (Confidential)",
            publicUsername = "ShadowWriter",
            pseudonym = "TheNightArchivist",
            anonymousNumberCode = "#4827",
            defaultPrivacyMode = PrivacyMode.ANONYMOUS_NUMBER,
            bio = "Investigating unexplained phenomena, archival anomalies, and midnight incident logs.",
            solvedMysteriesCount = 3,
            helpfulEvidenceCount = 14,
            activeCollaborationsCount = 2,
            followedTags = listOf("#Mystery", "#RealIncident", "#Unexplained", "#History", "#RadioCrypt"),
            blurSensitiveContent = true,
            selectedAppTheme = GenreTheme.MYSTERY
        )
        dao.insertOrUpdateUserSession(userSession)

        val posts = listOf(
            PostEntity(
                id = "post_1",
                title = "The Lights That Appeared Every Night on Ridge 9",
                content = """Every evening between 10:30 PM and 11:15 PM, an oscillating amber illumination appears on the unpaved crest of Ridge 9. There are no power lines, cell towers, or permitted residential dwellings within 14 kilometers.

Three separate local residents have documented the phenomenon across two seasons. The illumination does not track linearly like standard vehicular headlights; instead, it pulses at 1.4-second rhythmic intervals before shifting abruptly across the tree line. 

We set up two synchronized digital recording stations last Thursday to test the reflection hypothesis. What we found in the thermal and optical spectra complicates the initial explanations.""",
                type = PostType.MYSTERY,
                category = "Unexplained",
                authorName = "RidgeResearcher",
                authorHandle = "@ridgeline",
                identityMode = PrivacyMode.ANONYMOUS_NUMBER,
                anonymousNumberCode = "#4827",
                timestamp = "2 hours ago",
                readTimeMinutes = 5,
                readingProgress = 40,
                coverImageRes = "mystery_case_banner",
                voiceAudioDurationSeconds = 142,
                voiceTranscript = "Audio log station alpha: The thermal signature peaked at 10:48 PM. Ambient temperature was 4 degrees Celsius. The light source has no detectable heat bloom.",
                knownFacts = listOf(
                    "Pulses every 1.4 seconds between 10:30 PM and 11:15 PM",
                    "No municipal power grid or marked roads exist in the direct vector",
                    "Observed independently by 3 observers over 8 months"
                ),
                unknownFacts = listOf(
                    "Exact physical source or power mechanism",
                    "Why thermal cameras detect zero infrared bloom",
                    "Why the lights abruptly terminate precisely at 11:15 PM"
                ),
                questionToCommunity = "What physical optical condition or concealed apparatus could generate a pulsing 1.4s amber luminary with zero thermal signature?",
                mysteryStatus = MysteryStatus.INVESTIGATING,
                tags = listOf("#Mystery", "#Unexplained", "#Atmospheric", "#NightPhenomenon"),
                likesCount = 284,
                commentsCount = 42,
                savesCount = 98,
                sharesCount = 19,
                isLiked = true,
                isSaved = true,
                isArchived = false,
                isSensitive = false,
                sensitiveWarning = "",
                genreTheme = GenreTheme.MYSTERY,
                collaborationAllowed = true,
                collaborators = listOf("Anonymous #1102", "Anonymous #7821", "OpticsPro"),
                evidenceCount = 4,
                theoriesCount = 6,
                timelineCount = 5,
                verifiedBadge = true
            ),
            PostEntity(
                id = "post_2",
                title = "The Abandoned Highway 14 Transponder Incident",
                content = """While surveying the old closed bypass off Route 14 last month, our team discovered a decommissioned telecommunications relay box that had been rewired with non-standard copper coils and a modern solar accumulator.

The casing bore a stenciled alphanumeric code: 'PROJECT-AEGIS / SECTOR 4B'. When we connected an RTL-SDR spectrum analyzer, the unit was transmitting a repeating 16-tone sequence at 433.92 MHz every 18 minutes.

Please note: All sensitive private coordinates and identifying landowner details have been redacted in strict compliance with safety guidelines. We are seeking assistance in decoding the frequency structure.""",
                type = PostType.REAL_INCIDENT,
                category = "Real Incident",
                authorName = "SignalHunter",
                authorHandle = "@signalhunter",
                identityMode = PrivacyMode.PSEUDONYM,
                anonymousNumberCode = "#9912",
                timestamp = "5 hours ago",
                readTimeMinutes = 4,
                readingProgress = 0,
                coverImageRes = "incident_investigation_bg",
                voiceAudioDurationSeconds = 98,
                voiceTranscript = "Relay box recording: Audio sample taken from the demodulated FM carrier on 433.92 MHz. Repeating four-tone arpeggio.",
                knownFacts = listOf(
                    "Transmits on 433.92 MHz at exact 18-minute intervals",
                    "Unit contains customized analog filtration circuitry",
                    "Installed within the last 6 months based on battery manufacture date"
                ),
                unknownFacts = listOf(
                    "Origin and intent of the 16-tone repeating cipher",
                    "Entity behind the 'PROJECT-AEGIS' stencil"
                ),
                questionToCommunity = "Does anyone recognize the frequency modulation standard or the 16-tone encoding scheme?",
                mysteryStatus = MysteryStatus.STRONG_THEORY,
                tags = listOf("#RealIncident", "#RadioCrypt", "#Electronics", "#Hardware"),
                likesCount = 176,
                commentsCount = 29,
                savesCount = 63,
                sharesCount = 12,
                isLiked = false,
                isSaved = false,
                isArchived = false,
                isSensitive = false,
                sensitiveWarning = "",
                genreTheme = GenreTheme.CYBER,
                collaborationAllowed = true,
                collaborators = listOf("RadioHam88", "Anonymous #4827"),
                evidenceCount = 3,
                theoriesCount = 4,
                timelineCount = 4,
                verifiedBadge = false
            ),
            PostEntity(
                id = "post_3",
                title = "The Clockmaker of Kathmandu's Secret Partition",
                content = """In late 1968, a master horologist operating out of Asan Tole crafted a series of seventy-two brass maritime chronometers with double escapements. According to municipal archival manifests, twelve of these clocks were ordered by an unregistered foreign expedition.

When my grandfather acquired one of these movements in an estate liquidation three decades ago, he discovered a hollow gear chamber inside the balance wheel assembly. Inside was a rolled micro-film strip containing hand-drawn topographical contours of the Rolwaling Valley.

This story explores the historical intersection of Himalayan cartography, horological craftsmanship, and the quiet mysteries hidden inside vintage mechanics.""",
                type = PostType.STORY,
                category = "History & Archives",
                authorName = "HimalayanChronicle",
                authorHandle = "@himalaya",
                identityMode = PrivacyMode.REAL_PROFILE,
                anonymousNumberCode = "#1004",
                timestamp = "Yesterday",
                readTimeMinutes = 8,
                readingProgress = 75,
                coverImageRes = "archive_dossier_art",
                voiceAudioDurationSeconds = 210,
                voiceTranscript = "Oral history interview recorded in 1989 regarding the Asan horology workshop and the unmapped valley passes.",
                knownFacts = listOf(
                    "72 chronometers produced in 1968 workshop",
                    "Microfilm contains 1:50,000 scale hand-drawn contours",
                    "Watermark matches 1960s British Geodetic Survey paper"
                ),
                unknownFacts = listOf(
                    "Location of the remaining 11 custom escapement units",
                    "Identity of the patron who funded the expedition"
                ),
                questionToCommunity = "Are there any other collectors who have inspected the balance assembly of Asan Tole chronometers?",
                mysteryStatus = MysteryStatus.OPEN,
                tags = listOf("#History", "#Kathmandu", "#Archives", "#Cartography", "#Stories"),
                likesCount = 412,
                commentsCount = 38,
                savesCount = 180,
                sharesCount = 45,
                isLiked = true,
                isSaved = true,
                isArchived = false,
                isSensitive = false,
                sensitiveWarning = "",
                genreTheme = GenreTheme.ARCHIVE,
                collaborationAllowed = true,
                collaborators = listOf("KathmanduArchivist"),
                evidenceCount = 2,
                theoriesCount = 3,
                timelineCount = 3,
                verifiedBadge = true
            ),
            PostEntity(
                id = "post_4",
                title = "Why Digital Privacy Must Decouple Reputation from Identity",
                content = """Modern social architecture made a fatal structural assumption in 2008: that real names foster civility. Two decades of empirical data prove the opposite. Public identity creates performative vanity, hyper-partisanship, and personal vulnerability.

When identity is stripped away, content is forced to survive purely on its intellectual coherence and evidential weight. An anonymous number system allows collaborative investigation without the toxic baggage of personal branding or follower economics.

Here is why pseudonymity and dynamic anonymous identities are the future of collaborative truth-seeking.""",
                type = PostType.OPINION,
                category = "Opinions & Analysis",
                authorName = "Anonymous",
                authorHandle = "@anon",
                identityMode = PrivacyMode.COMPLETELY_ANONYMOUS,
                anonymousNumberCode = "#0000",
                timestamp = "1 day ago",
                readTimeMinutes = 4,
                readingProgress = 100,
                coverImageRes = "",
                voiceAudioDurationSeconds = 0,
                voiceTranscript = "",
                knownFacts = emptyList(),
                unknownFacts = emptyList(),
                questionToCommunity = "Should platforms permanently replace follower metrics with contribution reputation badges?",
                mysteryStatus = MysteryStatus.OPEN,
                tags = listOf("#Opinions", "#Privacy", "#DigitalRights", "#Philosophy"),
                likesCount = 530,
                commentsCount = 84,
                savesCount = 112,
                sharesCount = 34,
                isLiked = true,
                isSaved = false,
                isArchived = false,
                isSensitive = false,
                sensitiveWarning = "",
                genreTheme = GenreTheme.NOIR,
                collaborationAllowed = false,
                collaborators = emptyList(),
                evidenceCount = 0,
                theoriesCount = 0,
                timelineCount = 0,
                verifiedBadge = false
            ),
            PostEntity(
                id = "post_5",
                title = "Has anyone recorded this specific acoustic harmonic near underground vents?",
                content = """Around 02:45 AM during low traffic hours, several residents in the old industrial quarter have documented a low 54 Hz continuous acoustic drone that vibrates window panes. 

It does not align with standard HVAC compressor harmonics (which typically center on 60 Hz mains frequency). Here is the spectrum waterfall analysis recorded with a calibrated omnidirectional hydrophone placed near the storm intake.""",
                type = PostType.QUESTION,
                category = "Acoustics & Sensors",
                authorName = "AcousticAnon",
                authorHandle = "@acoustic",
                identityMode = PrivacyMode.ANONYMOUS_USERNAME,
                anonymousNumberCode = "#6129",
                timestamp = "2 days ago",
                readTimeMinutes = 3,
                readingProgress = 0,
                coverImageRes = "",
                voiceAudioDurationSeconds = 75,
                voiceTranscript = "Acoustic hydrophone feed filtered at 50-70 Hz bandpass. Audible low resonance note.",
                knownFacts = listOf(
                    "Fundamental frequency measured consistently at 53.8 Hz",
                    "Intensity peaks during winter nocturnal temperature inversions"
                ),
                unknownFacts = listOf(
                    "Subsurface piping resonance vs mechanical pump source"
                ),
                questionToCommunity = "What industrial or geological structure naturally resonates at 54 Hz?",
                mysteryStatus = MysteryStatus.OPEN,
                tags = listOf("#Acoustics", "#AudioEngineering", "#UrbanMystery", "#Questions"),
                likesCount = 120,
                commentsCount = 18,
                savesCount = 31,
                sharesCount = 6,
                isLiked = false,
                isSaved = false,
                isArchived = false,
                isSensitive = false,
                sensitiveWarning = "",
                genreTheme = GenreTheme.MINIMAL,
                collaborationAllowed = true,
                collaborators = listOf("Anonymous #4827"),
                evidenceCount = 2,
                theoriesCount = 3,
                timelineCount = 2,
                verifiedBadge = false
            )
        )
        dao.insertPosts(posts)

        // Seed Evidence for Post 1
        val evidenceList = listOf(
            EvidenceEntity(
                id = "ev_1",
                postId = "post_1",
                title = "Synchronized Dual Spectrograph & Lux Log",
                description = "High-precision lux meter readings capturing the 1.4-second pulse modulation cycle across 45 minutes on Nov 12.",
                type = EvidenceType.DOCUMENT,
                mediaResName = "archive_dossier_art",
                contributor = "OpticsPro",
                contributorIdentity = PrivacyMode.PSEUDONYM,
                dateAdded = "Nov 14",
                confidenceLevel = 94,
                upvotes = 52,
                verifiedByCreator = true
            ),
            EvidenceEntity(
                id = "ev_2",
                postId = "post_1",
                title = "Forward Looking Infrared (FLIR) Thermal Capture",
                description = "FLIR E8 thermal capture showing negative heat signature differential against the forest background.",
                type = EvidenceType.PHOTO,
                mediaResName = "incident_investigation_bg",
                contributor = "Anonymous #1102",
                contributorIdentity = PrivacyMode.ANONYMOUS_NUMBER,
                dateAdded = "Nov 15",
                confidenceLevel = 88,
                upvotes = 41,
                verifiedByCreator = true
            ),
            EvidenceEntity(
                id = "ev_3",
                postId = "post_1",
                title = "Aviation Transponder Radar Logs (FlightAware)",
                description = "FAA radar telemetry showing zero ADS-B flights or drone permits within 20nm at the time of illumination.",
                type = EvidenceType.SCREENSHOT,
                mediaResName = "mystery_case_banner",
                contributor = "AeroWatcher",
                contributorIdentity = PrivacyMode.ANONYMOUS_USERNAME,
                dateAdded = "Nov 16",
                confidenceLevel = 90,
                upvotes = 34,
                verifiedByCreator = false
            ),
            EvidenceEntity(
                id = "ev_4",
                postId = "post_1",
                title = "Local Topographic Gradient Chart",
                description = "10m elevation contour map showing steep ravine geometry behind the ridge where ground access is blocked.",
                type = EvidenceType.DOCUMENT,
                mediaResName = "archive_dossier_art",
                contributor = "Anonymous #4827",
                contributorIdentity = PrivacyMode.ANONYMOUS_NUMBER,
                dateAdded = "Nov 17",
                confidenceLevel = 82,
                upvotes = 19,
                verifiedByCreator = false
            )
        )
        dao.insertEvidenceList(evidenceList)

        // Seed Theories for Post 1
        val theories = listOf(
            TheoryEntity(
                id = "th_1",
                postId = "post_1",
                author = "PhysicistAnon",
                authorIdentity = PrivacyMode.ANONYMOUS_USERNAME,
                title = "Retroreflective Highway Signage Catching Distant Train Headlights",
                content = "A freight line runs through the valley 11 kilometers south. When a locomotive rounds the curve, its 200,000-candlepower headlight strikes a high-grade prismatic reflector on the weather tower, appearing as an amber pulse as trees sway.",
                supportCount = 68,
                challengeCount = 14,
                isAccepted = false,
                isDebunked = false,
                evidenceIds = listOf("ev_1", "ev_4"),
                timestamp = "1 day ago"
            ),
            TheoryEntity(
                id = "th_2",
                postId = "post_1",
                author = "Anonymous #7821",
                authorIdentity = PrivacyMode.ANONYMOUS_NUMBER,
                title = "Automated Wildlife Camera Strobe Array",
                content = "Department of Forestry researchers frequently place infrared / LED synchronized survey units that trigger at regular intervals during nocturnal tracking studies.",
                supportCount = 42,
                challengeCount = 29,
                isAccepted = false,
                isDebunked = false,
                evidenceIds = listOf("ev_2"),
                timestamp = "18 hours ago"
            ),
            TheoryEntity(
                id = "th_3",
                postId = "post_1",
                author = "OpticsPro",
                authorIdentity = PrivacyMode.PSEUDONYM,
                title = "Atmospheric Superior Mirage of Low-Pressure Sodium Lamps",
                content = "Under cold nocturnal temperature inversions, light rays bend over the mountain ridge, projecting the light of a quarry sodium lamp from over the horizon directly onto Ridge 9.",
                supportCount = 89,
                challengeCount = 6,
                isAccepted = false,
                isDebunked = false,
                evidenceIds = listOf("ev_1", "ev_2", "ev_3"),
                timestamp = "12 hours ago"
            )
        )
        dao.insertTheories(theories)

        // Seed Timeline for Post 1
        val timeline = listOf(
            TimelineEntity(
                id = "tl_1",
                postId = "post_1",
                timeLabel = "10:30 PM",
                title = "Initial Luminary Emergence",
                description = "First amber strobe detected above tree line coordinates (vector 314 deg NW).",
                isVerified = true,
                suggestedBy = "RidgeResearcher"
            ),
            TimelineEntity(
                id = "tl_2",
                postId = "post_1",
                timeLabel = "10:42 PM",
                title = "Pulse Rate Stabilization",
                description = "Oscillation locks onto precise 1.4-second interval recorded on dual optical sensors.",
                isVerified = true,
                suggestedBy = "OpticsPro"
            ),
            TimelineEntity(
                id = "tl_3",
                postId = "post_1",
                timeLabel = "10:48 PM",
                title = "Thermal Differential Measurement",
                description = "FLIR sensor records zero infrared bloom, ruling out open combustion or high-heat halogen.",
                isVerified = true,
                suggestedBy = "Anonymous #1102"
            ),
            TimelineEntity(
                id = "tl_4",
                postId = "post_1",
                timeLabel = "11:03 PM",
                title = "Angular Displacement Shift",
                description = "Apparent origin point shifts 4.2 degrees east without continuous transit path.",
                isVerified = false,
                suggestedBy = "Anonymous #7821"
            ),
            TimelineEntity(
                id = "tl_5",
                postId = "post_1",
                timeLabel = "11:15 PM",
                title = "Abrupt Cessation",
                description = "Light extinguishes completely. Ambient lux levels return to baseline 0.02.",
                isVerified = true,
                suggestedBy = "RidgeResearcher"
            )
        )
        dao.insertTimelineEvents(timeline)

        // Seed Comments for Post 1
        val comments = listOf(
            CommentEntity(
                id = "c_1",
                postId = "post_1",
                author = "OpticsPro",
                identityMode = PrivacyMode.PSEUDONYM,
                text = "The lack of thermal bloom is consistent with LED arrays or distant refracted light passing through an atmospheric inversion layer. I am deploying a grating spectrometer tomorrow night.",
                timestamp = "2 hours ago",
                likes = 34,
                isAcceptedAnswer = false,
                isTheory = true,
                isEvidenceSubmitted = true,
                audioDurationSeconds = 0,
                replyCount = 4
            ),
            CommentEntity(
                id = "c_2",
                postId = "post_1",
                author = "Anonymous #8910",
                identityMode = PrivacyMode.ANONYMOUS_NUMBER,
                text = "We witnessed identical pulses back in 2021 near the old quarry. Check the elevation coordinates against the limestone processing plant.",
                timestamp = "1 hour ago",
                likes = 19,
                isAcceptedAnswer = false,
                isTheory = false,
                isEvidenceSubmitted = false,
                audioDurationSeconds = 0,
                replyCount = 1
            )
        )
        dao.insertComments(comments)

        // Seed Groups
        val groups = listOf(
            AnonymousGroup(
                id = "grp_1",
                name = "Midnight Investigators",
                description = "A privacy-first collaborative syndicate investigating unexplained nocturnals, radar anomalies, and cold cases.",
                category = "Investigation Hub",
                memberCount = 1420,
                isPrivate = false,
                isAnonymousOnly = true,
                myAnonymousAlias = "Investigator #482",
                unreadCount = 3,
                pinnedNotice = "Case #104 (Ridge 9 Lights) is currently in Active Evidence Phase. Review the FLIR spectrum before proposing new theories.",
                activeCaseCount = 4
            ),
            AnonymousGroup(
                id = "grp_2",
                name = "Kathmandu Mystery Group",
                description = "Historical archival research, Himalayan expedition logs, rare manuscripts, and urban legends of Nepal.",
                category = "Historical & Archives",
                memberCount = 890,
                isPrivate = false,
                isAnonymousOnly = false,
                myAnonymousAlias = "Archivist #19",
                unreadCount = 0,
                pinnedNotice = "Upcoming translation workshop on 1960s Asan Horological Manuscripts this weekend.",
                activeCaseCount = 2
            ),
            AnonymousGroup(
                id = "grp_3",
                name = "Signal & Radio Cryptanalysis",
                description = "Shortwave radio listeners, SDR spectrum researchers, and digital signal decoding collective.",
                category = "Technology & Signals",
                memberCount = 640,
                isPrivate = false,
                isAnonymousOnly = true,
                myAnonymousAlias = "Cipher #99",
                unreadCount = 5,
                pinnedNotice = "Transponder frequency table updated for Route 14 incident.",
                activeCaseCount = 1
            ),
            AnonymousGroup(
                id = "grp_4",
                name = "Subterranean Urban Archives",
                description = "Mapping forgotten aqueducts, utility tunnels, civil defense shelters, and underground acoustics.",
                category = "Urban Exploration",
                memberCount = 512,
                isPrivate = true,
                isAnonymousOnly = true,
                myAnonymousAlias = "TunnelRat #07",
                unreadCount = 0,
                pinnedNotice = "Safety rule: Never explore active runoff drainage during rainfall warnings.",
                activeCaseCount = 2
            )
        )
        dao.insertGroups(groups.map {
            GroupEntity(
                id = it.id,
                name = it.name,
                description = it.description,
                category = it.category,
                memberCount = it.memberCount,
                isPrivate = it.isPrivate,
                isAnonymousOnly = it.isAnonymousOnly,
                myAnonymousAlias = it.myAnonymousAlias,
                unreadCount = it.unreadCount,
                pinnedNotice = it.pinnedNotice,
                activeCaseCount = it.activeCaseCount
            )
        })

        // Seed Direct Messages
        val messages = listOf(
            MessageEntity(
                id = "msg_1",
                conversationId = "conv_ridge",
                senderName = "OpticsPro",
                senderIdentity = PrivacyMode.PSEUDONYM,
                isMe = false,
                text = "Hello #4827. I saw your topographic chart on Ridge 9. Have you checked whether the power company ran test conduits there in 2019?",
                timestamp = "10:45 AM",
                mediaType = null,
                isRequest = false,
                audioDurationSeconds = 0
            ),
            MessageEntity(
                id = "msg_2",
                conversationId = "conv_ridge",
                senderName = "Anonymous #4827",
                senderIdentity = PrivacyMode.ANONYMOUS_NUMBER,
                isMe = true,
                text = "I checked the county permits. The closest easement ended 4 miles away due to the protected watershed boundary.",
                timestamp = "10:48 AM",
                mediaType = null,
                isRequest = false,
                audioDurationSeconds = 0
            ),
            MessageEntity(
                id = "msg_3",
                conversationId = "conv_ridge",
                senderName = "OpticsPro",
                senderIdentity = PrivacyMode.PSEUDONYM,
                isMe = false,
                text = "Fascinating. That makes an unauthorized solar beacon or optical reflection much more probable.",
                timestamp = "10:52 AM",
                mediaType = null,
                isRequest = false,
                audioDurationSeconds = 0
            ),
            // Message Request from unknown
            MessageEntity(
                id = "msg_req_1",
                conversationId = "conv_req_1",
                senderName = "Anonymous #9931",
                senderIdentity = PrivacyMode.ANONYMOUS_NUMBER,
                isMe = false,
                text = "I have uncompressed WAV audio of the 433 MHz transponder burst. Want me to upload it to the investigation board?",
                timestamp = "Yesterday",
                mediaType = "audio",
                isRequest = true,
                audioDurationSeconds = 32
            )
        )
        dao.insertMessages(messages)
    }
}
