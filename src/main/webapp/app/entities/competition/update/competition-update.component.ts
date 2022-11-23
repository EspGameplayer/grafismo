import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICompetition, Competition } from '../competition.model';
import { CompetitionService } from '../service/competition.service';
import { ISponsor } from 'app/entities/sponsor/sponsor.model';
import { SponsorService } from 'app/entities/sponsor/service/sponsor.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';
import { CompetitionType } from 'app/entities/enumerations/competition-type.model';

@Component({
  selector: 'jhi-competition-update',
  templateUrl: './competition-update.component.html',
})
export class CompetitionUpdateComponent implements OnInit {
  isSaving = false;
  competitionTypeValues = Object.keys(CompetitionType);

  sponsorsSharedCollection: ISponsor[] = [];
  competitionsSharedCollection: ICompetition[] = [];
  teamsSharedCollection: ITeam[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    graphicsName: [null, [Validators.required]],
    type: [null, [Validators.required]],
    colour: [null, [Validators.pattern('^[0-9a-fA-F]{6}$|^$')]],
    suspensionYcMatches: [null, [Validators.min(0)]],
    sponsor: [],
    motmSponsor: [],
    parent: [],
    teams: [],
  });

  constructor(
    protected competitionService: CompetitionService,
    protected sponsorService: SponsorService,
    protected teamService: TeamService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ competition }) => {
      this.updateForm(competition);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const competition = this.createFromForm();
    if (competition.id !== undefined) {
      this.subscribeToSaveResponse(this.competitionService.update(competition));
    } else {
      this.subscribeToSaveResponse(this.competitionService.create(competition));
    }
  }

  trackSponsorById(_index: number, item: ISponsor): number {
    return item.id!;
  }

  trackCompetitionById(_index: number, item: ICompetition): number {
    return item.id!;
  }

  trackTeamById(_index: number, item: ITeam): number {
    return item.id!;
  }

  getSelectedTeam(option: ITeam, selectedVals?: ITeam[]): ITeam {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompetition>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(competition: ICompetition): void {
    this.editForm.patchValue({
      id: competition.id,
      name: competition.name,
      graphicsName: competition.graphicsName,
      type: competition.type,
      colour: competition.colour,
      suspensionYcMatches: competition.suspensionYcMatches,
      sponsor: competition.sponsor,
      motmSponsor: competition.motmSponsor,
      parent: competition.parent,
      teams: competition.teams,
    });

    this.sponsorsSharedCollection = this.sponsorService.addSponsorToCollectionIfMissing(
      this.sponsorsSharedCollection,
      competition.sponsor,
      competition.motmSponsor
    );
    this.competitionsSharedCollection = this.competitionService.addCompetitionToCollectionIfMissing(
      this.competitionsSharedCollection,
      competition.parent
    );
    this.teamsSharedCollection = this.teamService.addTeamToCollectionIfMissing(this.teamsSharedCollection, ...(competition.teams ?? []));
  }

  protected loadRelationshipsOptions(): void {
    this.sponsorService
      .query()
      .pipe(map((res: HttpResponse<ISponsor[]>) => res.body ?? []))
      .pipe(
        map((sponsors: ISponsor[]) =>
          this.sponsorService.addSponsorToCollectionIfMissing(
            sponsors,
            this.editForm.get('sponsor')!.value,
            this.editForm.get('motmSponsor')!.value
          )
        )
      )
      .subscribe((sponsors: ISponsor[]) => (this.sponsorsSharedCollection = sponsors));

    this.competitionService
      .query()
      .pipe(map((res: HttpResponse<ICompetition[]>) => res.body ?? []))
      .pipe(
        map((competitions: ICompetition[]) =>
          this.competitionService.addCompetitionToCollectionIfMissing(competitions, this.editForm.get('parent')!.value)
        )
      )
      .subscribe((competitions: ICompetition[]) => (this.competitionsSharedCollection = competitions));

    this.teamService
      .query()
      .pipe(map((res: HttpResponse<ITeam[]>) => res.body ?? []))
      .pipe(map((teams: ITeam[]) => this.teamService.addTeamToCollectionIfMissing(teams, ...(this.editForm.get('teams')!.value ?? []))))
      .subscribe((teams: ITeam[]) => (this.teamsSharedCollection = teams));
  }

  protected createFromForm(): ICompetition {
    return {
      ...new Competition(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      graphicsName: this.editForm.get(['graphicsName'])!.value,
      type: this.editForm.get(['type'])!.value,
      colour: this.editForm.get(['colour'])!.value,
      suspensionYcMatches: this.editForm.get(['suspensionYcMatches'])!.value,
      sponsor: this.editForm.get(['sponsor'])!.value,
      motmSponsor: this.editForm.get(['motmSponsor'])!.value,
      parent: this.editForm.get(['parent'])!.value,
      teams: this.editForm.get(['teams'])!.value,
    };
  }
}
