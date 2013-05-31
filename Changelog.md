## yderimporter 4.3
*  Opgrading til sdm4-core 4.3, der løser
   NSPSUPPORT-126: ParserExecutor logger filers absolutte stier og md5-summer inden parser behandler dem

## yderimporter 4.4
* Fixed NSPSUPPORT-168
* Fixed NSPSUPPORT-190
* Don't importe a complete register everyday only update changes

Please note the tables below should be emptied before upgrade
Yderregister
YderregisterPerson
YderregisterKeyValue

## yderimporter 4.5
* Fixed SDM-35 Yderimport fejler
* Tilføjet indexer på Yderregister og YderregisterPerson så import og kopiregister service går markant hurtigere

## yderimporter 4.6
*  Opdateret SDM4 depencencies
*  SDM-5 SLA-log fra SDM4-importere følger ikke standarden
*  Tilføjet kopi register view, så kopi register service maps nu bliver oprettet automatisk
