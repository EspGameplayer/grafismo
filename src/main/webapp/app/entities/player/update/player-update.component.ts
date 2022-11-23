import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPlayer, Player } from '../player.model';
import { PlayerService } from '../service/player.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';
import { IPosition } from 'app/entities/position/position.model';
import { PositionService } from 'app/entities/position/service/position.service';
import { Foot } from 'app/entities/enumerations/foot.model';

@Component({
  selector: 'jhi-player-update',
  templateUrl: './player-update.component.html',
})
export class PlayerUpdateComponent implements OnInit {
  isSaving = false;
  footValues = Object.keys(Foot);

  peopleCollection: IPerson[] = [];
  teamsSharedCollection: ITeam[] = [];
  positionsSharedCollection: IPosition[] = [];

  editForm = this.fb.group({
    id: [],
    strongerFoot: [],
    height: [null, [Validators.min(0)]],
    weight: [null, [Validators.min(0)]],
    photo: [],
    photoContentType: [],
    person: [null, Validators.required],
    team: [],
    positions: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected playerService: PlayerService,
    protected personService: PersonService,
    protected teamService: TeamService,
    protected positionService: PositionService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ player }) => {
      this.updateForm(player);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('grafismoApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const player = this.createFromForm();
    if (player.id !== undefined) {
      this.subscribeToSaveResponse(this.playerService.update(player));
    } else {
      this.subscribeToSaveResponse(this.playerService.create(player));
    }
  }

  trackPersonById(_index: number, item: IPerson): number {
    return item.id!;
  }

  trackTeamById(_index: number, item: ITeam): number {
    return item.id!;
  }

  trackPositionById(_index: number, item: IPosition): number {
    return item.id!;
  }

  getSelectedPosition(option: IPosition, selectedVals?: IPosition[]): IPosition {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlayer>>): void {
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

  protected updateForm(player: IPlayer): void {
    this.editForm.patchValue({
      id: player.id,
      strongerFoot: player.strongerFoot,
      height: player.height,
      weight: player.weight,
      photo: player.photo,
      photoContentType: player.photoContentType,
      person: player.person,
      team: player.team,
      positions: player.positions,
    });

    this.peopleCollection = this.personService.addPersonToCollectionIfMissing(this.peopleCollection, player.person);
    this.teamsSharedCollection = this.teamService.addTeamToCollectionIfMissing(this.teamsSharedCollection, player.team);
    this.positionsSharedCollection = this.positionService.addPositionToCollectionIfMissing(
      this.positionsSharedCollection,
      ...(player.positions ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.personService
      .query({ filter: 'player-is-null' })
      .pipe(map((res: HttpResponse<IPerson[]>) => res.body ?? []))
      .pipe(map((people: IPerson[]) => this.personService.addPersonToCollectionIfMissing(people, this.editForm.get('person')!.value)))
      .subscribe((people: IPerson[]) => (this.peopleCollection = people));

    this.teamService
      .query()
      .pipe(map((res: HttpResponse<ITeam[]>) => res.body ?? []))
      .pipe(map((teams: ITeam[]) => this.teamService.addTeamToCollectionIfMissing(teams, this.editForm.get('team')!.value)))
      .subscribe((teams: ITeam[]) => (this.teamsSharedCollection = teams));

    this.positionService
      .query()
      .pipe(map((res: HttpResponse<IPosition[]>) => res.body ?? []))
      .pipe(
        map((positions: IPosition[]) =>
          this.positionService.addPositionToCollectionIfMissing(positions, ...(this.editForm.get('positions')!.value ?? []))
        )
      )
      .subscribe((positions: IPosition[]) => (this.positionsSharedCollection = positions));
  }

  protected createFromForm(): IPlayer {
    return {
      ...new Player(),
      id: this.editForm.get(['id'])!.value,
      strongerFoot: this.editForm.get(['strongerFoot'])!.value,
      height: this.editForm.get(['height'])!.value,
      weight: this.editForm.get(['weight'])!.value,
      photoContentType: this.editForm.get(['photoContentType'])!.value,
      photo: this.editForm.get(['photo'])!.value,
      person: this.editForm.get(['person'])!.value,
      team: this.editForm.get(['team'])!.value,
      positions: this.editForm.get(['positions'])!.value,
    };
  }
}
